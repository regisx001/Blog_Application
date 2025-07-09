package com.regisx001.blog.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.regisx001.blog.domain.entities.Article;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

    // Get articles related to a specific tag by tag name
    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tagName")
    Page<Article> findArticlesByTagName(@Param("tagName") String tagName, Pageable pageable);

}
