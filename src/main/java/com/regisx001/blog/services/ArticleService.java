package com.regisx001.blog.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.requests.CreateArticleRequest;
import com.regisx001.blog.domain.dto.requests.UpdateArticleRequest;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.User;

public interface ArticleService {
    public Page<ArticleDto> getAllArticles(Pageable pageable);

    Article createArticle(CreateArticleRequest articleRequest, User author);

    Article getArticleById(UUID id);

    Article updateArticle(UUID id, UpdateArticleRequest Article);

    void deleteArticle(UUID id);
}
