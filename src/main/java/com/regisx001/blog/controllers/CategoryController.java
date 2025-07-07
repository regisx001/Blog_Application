package com.regisx001.blog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.dto.CategoryDtoRef;
import com.regisx001.blog.domain.dto.requests.CreateCategoryRequest;
import com.regisx001.blog.domain.dto.requests.UpdateCategoryRequest;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.mappers.CategoryMapper;
import com.regisx001.blog.mappers.CategoryMapperRef;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final CategoryMapperRef categoryMapperRef;

    @GetMapping
    public ResponseEntity<Page<CategoryDtoRef.Detailed>> getAllCategories(Pageable pageable) {
        Page<CategoryDtoRef.Detailed> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDtoRef.Detailed> getCategoryById(@PathVariable UUID id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryMapperRef.toDetailedDto(category));
    }

    // @PostMapping
    // public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody
    // CreateCategoryRequest categoryRequest) {
    // Category category = categoryService.createCategory(categoryRequest);
    // return new ResponseEntity<>(categoryMapper.toDto(category),
    // HttpStatus.CREATED);
    // }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDtoRef.Detailed> createCategory(
            @Valid @ModelAttribute CategoryDtoRef.CreateWithImageRequest categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(categoryMapperRef.toDetailedDto(category), HttpStatus.CREATED);
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

    // Test endpoint to verify validation is working
    @PostMapping("/test-validation")
    public ResponseEntity<String> testValidation(@Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok("Validation passed: " + request.getTitle() + " - " + request.getDescription());
    }
}