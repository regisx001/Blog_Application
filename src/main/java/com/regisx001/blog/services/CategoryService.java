package com.regisx001.blog.services;

import java.util.List;
import java.util.UUID;

import com.regisx001.blog.domain.entities.Category;

public interface CategoryService {
    List<Category> listCategories();

    Category createCategory(Category category);

    void deleteCategory(UUID categoryId);
}
