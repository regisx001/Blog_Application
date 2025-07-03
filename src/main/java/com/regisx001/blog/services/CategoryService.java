package com.regisx001.blog.services;

import java.util.UUID;

import com.regisx001.blog.domain.dto.requests.CreateCategoryRequest;
import com.regisx001.blog.domain.entities.Category;;

public interface CategoryService {
    Category createCategory(CreateCategoryRequest categoryRequests);

    Category getCategoryById(UUID id);

    Category updateCategory(UUID id, Category category);

    void deleteCategory(UUID id);
}
