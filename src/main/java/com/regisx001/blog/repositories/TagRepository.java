package com.regisx001.blog.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByName(String name);

    @Query("SELECT t FROM Tag t LEFT JOIN t.articles a " +
            "GROUP BY t.id, t.name, t.slug, t.description, t.createdAt " +
            "ORDER BY COUNT(a) DESC, t.name ASC")
    Page<Tag> findAllTagsOrderByArticleCountDesc(Pageable pageable);

    // Get all tags with article count, sorted by count (ascending)
    @Query("SELECT t FROM Tag t LEFT JOIN t.articles a " +
            "GROUP BY t.id, t.name, t.slug, t.description, t.createdAt " +
            "ORDER BY COUNT(a) ASC, t.name ASC")
    Page<Tag> findAllTagsOrderByArticleCountAsc(Pageable pageable);

    // TODO: TO UNDERSTAND LATER (AI GENERATED)
    // Get articles related to a specific tag by tag ID
    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.id = :tagId")
    Page<Article> findArticlesByTagId(@Param("tagId") UUID tagId, Pageable pageable);

    // Get published articles only by tag name
    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tagName AND a.isPublished = true")
    Page<Article> findPublishedArticlesByTagName(@Param("tagName") String tagName, Pageable pageable);

    // Get articles by multiple tags
    @Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE t.name IN :tagNames")
    Page<Article> findArticlesByTagNames(@Param("tagNames") List<String> tagNames, Pageable pageable);

    // Count articles for a tag
    @Query("SELECT COUNT(a) FROM Article a JOIN a.tags t WHERE t.name = :tagName")
    Long countArticlesByTagName(@Param("tagName") String tagName);
}
