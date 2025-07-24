package com.regisx001.blog.repositories;

import java.util.List;
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

        @Query("SELECT a FROM Article a WHERE a.user.id = :authorId AND a.status IN (APPROVED, PENDING_REVIEW, REJECTED)")
        Page<Article> findArticlesSubmittedForReviewByUser(@Param("authorId") UUID authorId,
                        Pageable pageable);

        // @Query("SELECT a FROM Article a WHERE a.user.id = :authorId AND a.status IN
        // (APPROVED, PENDING, REJECTED)")
        // Page<Article> findArticlesSubmittedForReviewByUser(@Param("authorId") UUID
        // authorId, Pageable pageable);

        @Query("SELECT a FROM Article a WHERE " +
                        "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(a.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
        Page<Article> searchArticles(@Param("searchTerm") String searchTerm, Pageable pageable);

        @Query("SELECT a FROM Article a WHERE " +
                        "a.status = 'PUBLISHED' AND " +
                        "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(a.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<Article> searchPublishedArticles(@Param("searchTerm") String searchTerm, Pageable pageable);

        // ============= SEARCH FUNCTIONALITY =============

        // TODO: AI GENERATED UNDERSTAND LATER

        // Advanced search with multiple filters
        @Query("SELECT DISTINCT a FROM Article a LEFT JOIN a.tags t LEFT JOIN a.category c WHERE " +
                        "a.status = 'PUBLISHED' AND " +
                        "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(a.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<Article> searchPublishedArticlesAdvanced(@Param("searchTerm") String searchTerm, Pageable pageable);

        // Search by title only
        @Query("SELECT a FROM Article a WHERE " +
                        "a.status = 'PUBLISHED' AND " +
                        "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
        Page<Article> searchPublishedArticlesByTitle(@Param("searchTerm") String searchTerm, Pageable pageable);

        // Search with category filter
        @Query("SELECT a FROM Article a WHERE " +
                        "a.status = 'PUBLISHED' AND " +
                        "a.category.id = :categoryId AND " +
                        "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(a.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<Article> searchPublishedArticlesInCategory(@Param("searchTerm") String searchTerm,
                        @Param("categoryId") UUID categoryId,
                        Pageable pageable);

        // Search with tag filter
        @Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE " +
                        "a.status = 'PUBLISHED' AND " +
                        "t.name = :tagName AND " +
                        "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(a.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<Article> searchPublishedArticlesWithTag(@Param("searchTerm") String searchTerm,
                        @Param("tagName") String tagName,
                        Pageable pageable);

        // Search user's articles (for authenticated users)
        @Query("SELECT a FROM Article a WHERE " +
                        "a.user.id = :authorId AND " +
                        "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(a.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<Article> searchUserArticles(@Param("searchTerm") String searchTerm,
                        @Param("authorId") UUID authorId,
                        Pageable pageable);

        // Full-text search suggestions (PostgreSQL specific)
        @Query(value = "SELECT DISTINCT a.* FROM articles a WHERE " +
                        "to_tsvector('english', a.title || ' ' || a.content) @@ plainto_tsquery('english', :searchTerm) "
                        +
                        "AND a.status = 'PUBLISHED' " +
                        "ORDER BY ts_rank(to_tsvector('english', a.title || ' ' || a.content), plainto_tsquery('english', :searchTerm)) DESC", nativeQuery = true)
        Page<Article> fullTextSearchPublishedArticles(@Param("searchTerm") String searchTerm, Pageable pageable);

        // Get search suggestions based on existing article titles
        @Query("SELECT DISTINCT a.title FROM Article a WHERE " +
                        "a.status = 'PUBLISHED' AND " +
                        "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
                        "ORDER BY a.title")
        List<String> getSearchSuggestions(@Param("searchTerm") String searchTerm);
}
