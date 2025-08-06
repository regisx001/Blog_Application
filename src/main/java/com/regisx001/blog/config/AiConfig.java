package com.regisx001.blog.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public AIAnalysisConfig aiAnalysisConfig() {
        return AIAnalysisConfig.builder()
                .minWordCount(30)
                .maxWordCount(10000)
                .confidenceThreshold(0.65)
                .autoApprovalThreshold(0.80)
                .autoRejectionThreshold(0.30)
                .plagiarismThreshold(0.85)
                .grammarScoreThreshold(0.70)
                .readabilityScoreThreshold(0.60)
                .build();
    }

    @Bean
    ChatClient ChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).defaultSystem("""
                You are an expert content reviewer and administrator for a blog platform.
                Your responsibilities include:
                - Analyzing articles for quality, appropriateness, and adherence to editorial standards.
                - Generating relevant content tags (e.g., topic, sentiment, audience).
                - Flagging and reporting any content that violates platform policies or legal requirements.
                - Providing constructive feedback and specific recommendations for improvement.
                - Ensuring articles are suitable for the intended audience and comply with all guidelines.
                If a specific response format is demanded, always respond in that format.
                """).build();
    }
}