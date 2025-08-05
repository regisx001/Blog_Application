package com.regisx001.blog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.services.ArticleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(path = "/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleDto.Detailed>> getAllArticles(Pageable pageable) {
        return ResponseEntity.ok(articleService.getPublishedArticles(pageable));
    }

    @GetMapping(path = "/search/{searchTerms}")
    public ResponseEntity<Page<ArticleDto.Detailed>> searchArticles(@PathVariable String searchTerms,
            Pageable pageable) {
        return ResponseEntity.ok(articleService.searchArticles(searchTerms, pageable));
    }

    @GetMapping(path = "/my-articles")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ArticleDto.Detailed>> getArticlesByUser(Pageable pageable,
            @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.getArticlesByUser(userDetails.getId(), pageable));
    }

    @GetMapping(path = "/my-drafts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ArticleDto.Detailed>> getDraftArticlesByUser(Pageable pageable,
            @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.getDraftArticlesByUser(userDetails.getId(), pageable));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getArticle(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        // return ResponseEntity.ok(articleService.getArticleById(id));
        return ResponseEntity.ok(articleService.getArticleByIdAndUser(id, user));

    }

    // TODO: FIX AND MERGE THIS WITH THE ONE ABOVE
    @GetMapping(path = "/id-user/{id}")
    public ResponseEntity<?> getArticleByIdAndUser(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.getArticleByIdAndUser(id, user));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticleDto.Detailed> createArticle(
            @Valid @ModelAttribute ArticleDto.CreateRequest createRequest,
            @AuthenticationPrincipal User userDetails) {
        return new ResponseEntity<>(articleService.createArticle(createRequest, userDetails.getId()),
                HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/batch")
    public ResponseEntity<?> deleteArticlesInBatch(
            @ModelAttribute ArticleDto.DeleteInBatchRequest deleteInBatchRequest) {
        articleService.deleteArticlesInBatchById(deleteInBatchRequest.ids());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-review/{id}")
    public ResponseEntity<?> sendArticleForReview(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.sendForReview(id, userDetails.getId()));
    }

    @PostMapping("/unsend-review/{id}")
    public ResponseEntity<?> unsendArticleForReview(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.unsendForReview(id, userDetails.getId()));
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<?> publishArticle(@PathVariable UUID id,
            @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.publishArticle(id,
                userDetails.getId()));
    }

    @PostMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishArticle(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.unpublishArticle(id, userDetails.getId()));
    }

    @PostMapping("/convert-draft/{id}")
    public ResponseEntity<?> convertArticleToDraft(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.draftArticle(id, userDetails));
    }

    @GetMapping(path = "/for-review")
    public ResponseEntity<?> getReviewedArticlesByUser(@AuthenticationPrincipal User user, Pageable pageable) {
        return ResponseEntity.ok(articleService.getReviewArticlesByUser(user.getId(), pageable));
    }

    // ==================== ADMINS-ENDPOINTS ======================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/admin")
    public ResponseEntity<Page<ArticleDto.Detailed>> getAllArticlesAdmins(Pageable pageable) {
        return ResponseEntity.ok(articleService.getAllArticles(pageable));
    }
}
