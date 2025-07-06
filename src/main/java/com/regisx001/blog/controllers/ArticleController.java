package com.regisx001.blog.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.requests.CreateArticleRequest;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.services.ArticleService;
import com.regisx001.blog.services.CategoryService;
import com.regisx001.blog.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;
    private final ArticleMapper articleMapper;
    // private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody CreateArticleRequest createArticleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        Article article = articleService.createArticle(createArticleRequest, currentUser);

        return ResponseEntity.ok(articleMapper.toDto(article));
    }

}
