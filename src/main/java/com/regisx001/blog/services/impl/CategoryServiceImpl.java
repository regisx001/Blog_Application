package com.regisx001.blog.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.mappers.CategoryMapper;
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

    @Override
    public Page<CategoryDto.Detailed> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDetailedDto);
    }

    @Override
    public Category createCategory(CategoryDto.CreateWithImageRequest categoryRequest) {
        String imagePath = null;

        if (categoryRequest.image() != null && !categoryRequest.image().isEmpty()) {
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
    public Category updateCategory(UUID id, CategoryDto.UpdateRequest category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        existingCategory.setTitle(category.title());
        existingCategory.setDescription(category.description());
        existingCategory.setImage(category.image());
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

    @Override
    public List<String> getCategoriesTitles() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::getTitle)
                .toList();
    }

}
