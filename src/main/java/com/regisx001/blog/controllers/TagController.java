package com.regisx001.blog.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.services.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> getTags(Pageable pageable) {
        return ResponseEntity.ok(tagService.getAllTagsWithCount(pageable));
    }

    @GetMapping(path = "/{tagName}")
    public ResponseEntity<?> getRelatedArticles(Pageable pageable, @PathVariable String tagName) {

        return ResponseEntity.ok(tagService.getTagRelatedArticle(pageable, tagName));
    }
}
