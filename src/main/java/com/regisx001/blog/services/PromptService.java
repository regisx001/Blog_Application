package com.regisx001.blog.services;

import com.regisx001.blog.domain.entities.Article;

public interface PromptService {
    String buildArticleAnalysisPrompt(Article article);
}
