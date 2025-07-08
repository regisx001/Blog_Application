package com.regisx001.blog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.services.ArticleService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleDto.Basic>> getAllArticles(Pageable pageable) {
        return ResponseEntity.ok(articleService.getAllBasicArticles(pageable));
    }

    @PostMapping
    public ResponseEntity<ArticleDto.Detailed> createArticle(@RequestBody ArticleDto.CreateRequest createRequest,
            @AuthenticationPrincipal User userDetails) {
        return new ResponseEntity<>(articleService.createArticle(createRequest, userDetails.getId()),
                HttpStatus.CREATED);
    }

}
