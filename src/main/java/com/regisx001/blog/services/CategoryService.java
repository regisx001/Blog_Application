package com.regisx001.blog.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.entities.Category;;

public interface CategoryService {
    public Page<CategoryDto.Detailed> getAllCategories(Pageable pageable);

    Category createCategory(CategoryDto.CreateWithImageRequest categoryRequests);

    Category getCategoryById(UUID id);

    Category findByTitle(String title);

    Category updateCategory(UUID id, CategoryDto.UpdateRequest category);

    void deleteCategory(UUID id);
}
