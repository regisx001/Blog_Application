package com.regisx001.blog.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.dto.requests.CreateCategoryRequest;
import com.regisx001.blog.domain.dto.requests.UpdateCategoryRequest;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.mappers.CategoryMapper;
import com.regisx001.blog.repositories.CategoryRepository;
import com.regisx001.blog.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public Category createCategory(CreateCategoryRequest categoryRequest) {
        Category category = Category.builder().title(categoryRequest.getTitle())
                .description(categoryRequest.getDescription()).image(categoryRequest.getImage()).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        return existingCategory;
    }

    @Override
    public Category updateCategory(UUID id, UpdateCategoryRequest category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        existingCategory.setTitle(category.getTitle());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setImage(category.getImage());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(UUID id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        categoryRepository.delete(existingCategory);
    }

}
