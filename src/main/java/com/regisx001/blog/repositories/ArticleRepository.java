package com.regisx001.blog.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Enums.ArticleStatus;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Page<Article> findArticlesByStatus(ArticleStatus status, Pageable pageable);

    // Get articles related to a specific tag by tag name
    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tagName")
    Page<Article> findArticlesByTagName(@Param("tagName") String tagName, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.user.id = :authorId")
    Page<Article> findArticlesByUserId(@Param("authorId") UUID authorId, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.user.id = :authorId AND a.status = :status")
    Page<Article> findArticlesByUserIdAndStatus(@Param("authorId") UUID authorId,
            @Param("status") ArticleStatus status,
            Pageable pageable);
}
