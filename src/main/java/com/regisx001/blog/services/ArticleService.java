package com.regisx001.blog.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.ArticleDto;

public interface ArticleService {

    // ============= GET OPERATIONS =============

    Page<ArticleDto.Detailed> getAllArticles(Pageable pageable);

    ArticleDto.Detailed getArticleById(UUID id);

    // ============= CRUD OPERATIONS =============

    ArticleDto.Detailed createArticle(ArticleDto.CreateRequest request, UUID authorId);

    ArticleDto.Detailed updateArticle(UUID id, ArticleDto.UpdateRequest request, UUID authorId);

    void deleteArticle(UUID id, UUID authorId);

    // ============= PUBLISH OPERATIONS =============

    ArticleDto.Detailed publishArticle(UUID id, UUID authorId);

    ArticleDto.Detailed unpublishArticle(UUID id, UUID authorId);

    // ============= USER-SPECIFIC OPERATIONS =============

    Page<ArticleDto.Draft> getUserDrafts(UUID authorId, Pageable pageable);

    Page<ArticleDto.Summary> getPublishedArticles(Pageable pageable);
}