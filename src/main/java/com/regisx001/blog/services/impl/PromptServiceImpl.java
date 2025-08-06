package com.regisx001.blog.services.impl;

import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.services.PromptService;
import com.regisx001.blog.utils.AIPromptTemplates;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromptServiceImpl implements PromptService {

    @Override
    public String buildArticleAnalysisPrompt(Article article) {
        return AIPromptTemplates.CONTENT_ANALYSIS_PROMPT
                .replace("{title}", article.getTitle())
                .replace("{content}", article.getContent());
    }

}
