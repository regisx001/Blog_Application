package com.regisx001.blog.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Category;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    public Optional<Category> findByTitle(String title);

    @Query("SELECT a FROM Article a WHERE a.category.id = :categoryId ORDER BY a.createdAt DESC")
    Page<Article> findArticlesByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.category.title = :categoryTitle ORDER BY a.createdAt DESC")
    Page<Article> findArticlesByCategoryTitle(@Param("categoryTitle") String categoryTitle, Pageable pageable);
}
