package com.regisx001.blog.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.dto.CategoryDtoRef;
import com.regisx001.blog.domain.dto.requests.CreateCategoryRequest;
import com.regisx001.blog.domain.dto.requests.UpdateCategoryRequest;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.mappers.CategoryMapper;
import com.regisx001.blog.mappers.CategoryMapperRef;
import com.regisx001.blog.repositories.CategoryRepository;
import com.regisx001.blog.services.CategoryService;
import com.regisx001.blog.services.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final StorageService storageService;
    private final CategoryMapper categoryMapper;
    private final CategoryMapperRef categoryMapperRef;

    @Override
    public Page<CategoryDtoRef.Detailed> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapperRef::toDetailedDto);
    }

    @Override
    public Category createCategory(CategoryDtoRef.CreateWithImageRequest categoryRequest) {
        String imagePath = null;

        if (categoryRequest.image() != null) {
            try {
                imagePath = storageService.store(categoryRequest.image());
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
            }
        }

        Category category = Category.builder()
                .title(categoryRequest.title())
                .description(categoryRequest.description())
                .image(imagePath) // This will be null if no image uploaded
                .build();

        // Category category = Category.builder().title(categoryRequest.getTitle())
        // .description(categoryRequest.getDescription()).image(categoryRequest.getImage()).build();
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

    // TODO: FUTURE USE
    // private String generateSlug(String title) {
    // return title.toLowerCase()
    // .replaceAll("[^a-z0-9\\s]", "")
    // .replaceAll("\\s+", "-")
    // .trim();
    // }

    @Override
    public Category findByTitle(String title) {
        return categoryRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Category not found"));
    }

}
