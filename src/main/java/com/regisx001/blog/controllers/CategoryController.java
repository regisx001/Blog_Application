package com.regisx001.blog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.mappers.CategoryMapper;
import com.regisx001.blog.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<Page<CategoryDto.Basic>> getAllCategories(Pageable pageable) {
        Page<CategoryDto.Basic> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/titles")
    public ResponseEntity<?> getAllCategoriesTitles() {
        return ResponseEntity.ok(categoryService.getCategoriesTitles());
    }

    @GetMapping("/titles/search/{searchTerm}")
    public ResponseEntity<?> searchCategoriesTitles(@PathVariable String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(categoryService.searchCategoriesTitles(searchTerm, pageable));
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<CategoryDto.Detailed> getCategoryById(@PathVariable
    // UUID id) {
    // Category category = categoryService.getCategoryById(id);
    // return ResponseEntity.ok(categoryMapper.toDetailedDto(category));
    // }

    @GetMapping("/{title}")
    public ResponseEntity<CategoryDto.Detailed> getCategoryByTitle(@PathVariable String title) {
        Category category = categoryService.getCategoryByTitle(title);
        return ResponseEntity.ok(categoryMapper.toDetailedDto(category));
    }

    @GetMapping("/{title}/articles")
    public ResponseEntity<Page<ArticleDto.Detailed>> getArticlesByCategoryId(@PathVariable String title,
            Pageable pageable) {
        return ResponseEntity.ok(categoryService.getCategoryRelatedArticles(title, pageable));
    }

    // @PostMapping
    // public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody
    // CreateCategoryRequest categoryRequest) {
    // Category category = categoryService.createCategory(categoryRequest);
    // return new ResponseEntity<>(categoryMapper.toDto(category),
    // HttpStatus.CREATED);
    // }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDto.Detailed> createCategory(
            @Valid @ModelAttribute CategoryDto.CreateWithImageRequest categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(categoryMapper.toDetailedDto(category), HttpStatus.CREATED);
    }

    // TODO: IMPLEMENT LATER
    // @PutMapping("/{id}")
    // public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID id,
    // @Valid @RequestBody UpdateCategoryRequest categoryRequest) {
    // Category category = categoryService.updateCategory(id, categoryRequest);
    // return ResponseEntity.ok(categoryMapper.toDto(category));
    // }

    @DeleteMapping("/{id}") // Fixed: was "/id", now "/{id}"
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}