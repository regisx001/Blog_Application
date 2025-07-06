package com.regisx001.blog.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.regisx001.blog.domain.entities.Article;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

}
