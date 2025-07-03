package com.regisx001.blog.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.dto.requests.CreateCategoryRequest;
import com.regisx001.blog.domain.dto.requests.UpdateCategoryRequest;
import com.regisx001.blog.domain.entities.Category;;

public interface CategoryService {
    public Page<CategoryDto> getAllCategories(Pageable pageable);

    Category createCategory(CreateCategoryRequest categoryRequests);

    Category getCategoryById(UUID id);

    Category updateCategory(UUID id, UpdateCategoryRequest category);

    void deleteCategory(UUID id);
}
