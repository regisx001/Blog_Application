package com.regisx001.blog.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.entities.Category;;

public interface CategoryService {
    Page<CategoryDto.Basic> getAllCategories(Pageable pageable);

    List<String> getCategoriesTitles();

    Category createCategory(CategoryDto.CreateWithImageRequest categoryRequests);

    Category getCategoryById(UUID id);

    Page<ArticleDto.Detailed> getCategoryRelatedArticles(UUID id, Pageable pageable);

    Category findByTitle(String title);

    Category updateCategory(UUID id, CategoryDto.UpdateRequest category);

    void deleteCategory(UUID id);
}
