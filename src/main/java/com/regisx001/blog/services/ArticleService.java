package com.regisx001.blog.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.domain.entities.Enums.ArticleStatus;

public interface ArticleService {

    // ============= GET OPERATIONS =============

    Page<ArticleDto.Detailed> getPublishedArticles(Pageable pageable);

    ArticleDto.Detailed getArticleById(UUID id);

    ArticleDto.Detailed getArticleByIdAndUser(UUID id, User user);

    Page<ArticleDto.Detailed> getArticlesByUser(UUID userId, Pageable pageable);

    Page<ArticleDto.Detailed> getDraftArticlesByUser(UUID userId, Pageable pageable);

    Page<ArticleDto.Detailed> searchArticles(String searchTerms, Pageable pageable);

    // ============= CRUD OPERATIONS =============

    ArticleDto.Detailed createArticle(ArticleDto.CreateRequest request, UUID authorId);

    ArticleDto.Detailed updateArticle(UUID id, ArticleDto.UpdateRequest request, UUID authorId);

    void deleteArticle(UUID id, UUID authorId);

    void deleteArticlesInBatchById(Iterable<UUID> ids);

    // ============= PUBLISH OPERATIONS =============
    ArticleDto.Detailed sendForReview(UUID id, UUID authorId);

    ArticleDto.Detailed unsendForReview(UUID id, UUID authorId);

    ArticleDto.Detailed publishArticle(UUID id, UUID authorId);

    ArticleDto.Detailed unpublishArticle(UUID id, UUID authorId);

    ArticleDto.Detailed approveArticle(UUID id, ArticleDto.ApproveRequest approveRequest, User adminUser);

    ArticleDto.Detailed rejectArticle(UUID id, ArticleDto.RejectionRequest rejectionRequest, User adminUser);

    // ============= USER-SPECIFIC OPERATIONS =============

    Page<ArticleDto.Draft> getUserDrafts(UUID authorId, Pageable pageable);

    // ============= ADMINISTRATION SPECIFIC OPERATIONS =============

    Page<ArticleDto.Detailed> getAllArticles(Pageable pageable);

    Page<ArticleDto.Detailed> getArticlesByStatus(ArticleStatus status, Pageable pageable);

}