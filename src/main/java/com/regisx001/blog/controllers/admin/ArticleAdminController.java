package com.regisx001.blog.controllers.admin;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.domain.entities.Enums.ArticleStatus;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.services.ArticleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/admin/articles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ArticleAdminController {
    private final ArticleMapper articleMapper;
    private final ArticleService articleService;

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ArticleDto.Detailed>> getArticlesByStatus(
            @PathVariable ArticleStatus status, Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticlesByStatus(status, pageable));
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<?> publishArticle(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.publishArticle(id, userDetails.getId()));
    }

    @PostMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishArticle(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.unpublishArticle(id, userDetails.getId()));
    }

}
