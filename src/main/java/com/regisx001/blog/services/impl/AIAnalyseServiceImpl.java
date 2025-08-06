package com.regisx001.blog.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regisx001.blog.config.AIAnalysisConfig;
import com.regisx001.blog.domain.dto.responses.AIAnalysisResponse;
import com.regisx001.blog.domain.entities.AnalyseHistory;
import com.regisx001.blog.domain.entities.AnalyseResult;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Enums.AnalyseDecision;
import com.regisx001.blog.domain.entities.Enums.ArticleStatus;
import com.regisx001.blog.repositories.AnalyseHistoryRepository;
import com.regisx001.blog.repositories.AnalyseResultRepository;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.services.AIAnalyseService;
import com.regisx001.blog.services.PromptService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AIAnalyseServiceImpl implements AIAnalyseService {

    private final PromptService promptService;
    private final AIAnalysisConfig config;
    private final AnalyseResultRepository analyseResultRepository;
    private final ArticleRepository articleRepository;
    private final AnalyseHistoryRepository analyseHistoryRepository;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    @Async
    @Override
    public void analyseArticle(UUID id) {
        analyse(id);
        // return result;
    }

    @Override
    public AnalyseResult getLatestApprovalResult(UUID id) {
        AnalyseResult result = analyseResultRepository.findFirstByArticleIdOrderByAnalyzedAtDesc(id)
                .orElseThrow(() -> new RuntimeException("No Result for this article"));
        return result;
    }

    @Override
    public AnalyseResult analyseArticleManual(UUID id) {
        return analyse(id);
    }

    private AnalyseResult analyse(UUID id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        AnalyseHistory historySnapshot = new AnalyseHistory();

        // HISTORY RELATED
        historySnapshot.setArticle(article);
        historySnapshot.setAiModel(model);
        historySnapshot.setFromStatus(article.getStatus());
        historySnapshot.setPerformedBy("AI System");
        // ---------------

        long startTime = System.currentTimeMillis();
        if (isValidForAnalysis(article)) {
            throw new RuntimeException("Article doesn't meet basic requirements by system-analysis");
        }

        String prompt = promptService.buildArticleAnalysisPrompt(article);
        String aiResponse = chatClient.prompt(prompt).call().content();
        long endTime = System.currentTimeMillis();
        Integer analyzeTimeMs = (int) (endTime - startTime);

        AnalyseResult result = buildApprovalResult(article, parseAIResponse(aiResponse), analyzeTimeMs);
        // article.setStatus(ArticleStatus.valueOf(result.getDecision().toString()));
        if (result.getDecision().equals(AnalyseDecision.APPROVED)) {
            article.setApprovedAt(LocalDateTime.now());
            article.setStatus(ArticleStatus.APPROVED);
            article.setApprovedBy("AI System");
        } else if (result.getDecision().equals(AnalyseDecision.REJECTED)) {
            article.setRejectedAt(LocalDateTime.now());
            article.setStatus(ArticleStatus.REJECTED);
            article.setRejectedBy("AI System");
        } else if (result.getDecision().equals(AnalyseDecision.REQUIRES_MANUAL_REVIEW)) {
            article.setStatus(ArticleStatus.PENDING_REVIEW);
        }

        // HISTORY RELATED
        historySnapshot.setToStatus(article.getStatus());
        historySnapshot.setNotes(result.getRecommendations());
        historySnapshot.setConfidenceScore(result.getConfidenceScore());
        historySnapshot.setProcessingTimeMs(analyzeTimeMs);
        // ---------------

        article.setFeedback(result.getAiAnalysis());
        articleRepository.save(article);
        analyseHistoryRepository.save(historySnapshot);
        return analyseResultRepository.save(result);
    }

    private boolean isValidForAnalysis(Article article) {
        int wordCount = article.getContent().split("\\s+").length;
        return wordCount >= config.getMinWordCount() &&
                wordCount <= config.getMaxWordCount() &&
                article.getTitle() != null && !article.getTitle().trim().isEmpty();
    }

    private AIAnalysisResponse parseAIResponse(String response) {
        try {
            return objectMapper.readValue(response, AIAnalysisResponse.class);
        } catch (Exception e) {
            return AIAnalysisResponse.builder()
                    .overallScore(0.5) // Neutral score
                    .recommendation("NEEDS_MANUAL_REVIEW")
                    .feedback("AI analysis completed but requires manual review. Raw response: " + response)
                    .recommendations(List.of("Manual review required"))
                    .build();
        }
    }

    private AnalyseDecision determineDecision(AIAnalysisResponse response) {
        double score = response.getOverallScore();

        if (score >= config.getAutoApprovalThreshold()) {
            return AnalyseDecision.APPROVED;
        } else if (score <= config.getAutoRejectionThreshold()) {
            return AnalyseDecision.REJECTED;
        } else {
            return AnalyseDecision.REQUIRES_MANUAL_REVIEW;
        }
    }

    private AnalyseResult buildApprovalResult(Article article, AIAnalysisResponse response, Integer processingTimeMs) {
        AnalyseDecision decision = determineDecision(response);

        // Get feedback from root level or combine nested feedback
        String aiAnalysis = response.getFeedback();
        if (aiAnalysis == null || aiAnalysis.trim().isEmpty()) {
            StringBuilder combinedFeedback = new StringBuilder();
            if (response.getContentQuality() != null && response.getContentQuality().getFeedback() != null) {
                combinedFeedback.append("Content Quality: ").append(response.getContentQuality().getFeedback())
                        .append(". ");
            }
            if (response.getGrammar() != null && response.getGrammar().getFeedback() != null) {
                combinedFeedback.append("Grammar: ").append(response.getGrammar().getFeedback()).append(". ");
            }
            if (response.getAppropriateness() != null && response.getAppropriateness().getFeedback() != null) {
                combinedFeedback.append("Appropriateness: ").append(response.getAppropriateness().getFeedback())
                        .append(".");
            }
            aiAnalysis = combinedFeedback.toString().trim();
        }

        // Convert recommendations list to string
        String recommendationsStr = "";
        if (response.getRecommendations() != null && !response.getRecommendations().isEmpty()) {
            recommendationsStr = String.join("; ", response.getRecommendations());
        }

        return AnalyseResult.builder()
                .article(article)
                .decision(decision)
                .confidenceScore(response.getOverallScore())
                .aiAnalysis(aiAnalysis)
                .recommendations(recommendationsStr)
                .readabilityScore(response.getContentQuality() != null ? response.getContentQuality().getScore() : null)
                .grammarScore(response.getGrammar() != null ? response.getGrammar().getScore() : null)
                .seoScore(response.getSeo() != null ? response.getSeo().getScore() : null)
                .aiModel(model)
                .processingTimeMs(processingTimeMs)
                .analyzedAt(LocalDateTime.now())
                .build();
    }

}