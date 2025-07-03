package com.regisx001.blog.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.regisx001.blog.domain.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
