package com.regisx001.blog.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.requests.CreateCategoryRequest;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.repositories.CategoryRepository;
import com.regisx001.blog.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CreateCategoryRequest categoryRequest) {
        Category category = Category.builder().title(categoryRequest.getTitle())
                .description(categoryRequest.getDescription()).image(categoryRequest.getImage()).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoryById'");
    }

    @Override
    public Category updateCategory(UUID id, Category category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCategory'");
    }

    @Override
    public void deleteCategory(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCategory'");
    }

}
