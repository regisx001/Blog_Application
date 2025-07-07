package com.regisx001.blog.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.dto.CategoryDtoRef;
import com.regisx001.blog.domain.dto.requests.CreateCategoryRequest;
import com.regisx001.blog.domain.dto.requests.UpdateCategoryRequest;
import com.regisx001.blog.domain.entities.Category;;

public interface CategoryService {
    public Page<CategoryDtoRef.Detailed> getAllCategories(Pageable pageable);

    Category createCategory(CategoryDtoRef.CreateWithImageRequest categoryRequests);

    Category getCategoryById(UUID id);

    Category findByTitle(String title);

    Category updateCategory(UUID id, UpdateCategoryRequest category);

    void deleteCategory(UUID id);
}
