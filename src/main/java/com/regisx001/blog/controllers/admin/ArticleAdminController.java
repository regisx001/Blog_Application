package com.regisx001.blog.controllers.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.domain.entities.Enums.ArticleStatus;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.services.ArticleService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/admin/articles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ArticleAdminController {
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @GetMapping
    public ResponseEntity<Page<ArticleDto.Detailed>> getAllArticles(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String status,
            Pageable pageable) {

        ArticleStatus articleStatus = null;
        if (status != null) {
            try {
                articleStatus = ArticleStatus.valueOf(status); // Convert String to RoleType enum
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role: " + status);
            }
        }
        return ResponseEntity.ok(articleService.getAllArticlesByFilters(searchTerm, articleStatus, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto.Detailed> getArticle(@PathVariable UUID id,
            @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.getArticleByIdAndUser(id, userDetails)); // Admin can see any article
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ArticleDto.Detailed>> getArticlesByStatus(
            @PathVariable ArticleStatus status, Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticlesByStatus(status, pageable));
    }

    @GetMapping("/pending-review")
    public ResponseEntity<Page<ArticleDto.Detailed>> getPendingReviewArticles(Pageable pageable) {
        return ResponseEntity.ok(articleService.getArticlesByStatus(ArticleStatus.PENDING_REVIEW, pageable));
    }

    @PostMapping(path = "/approve/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticleDto.Detailed> approveArticle(@PathVariable UUID id,
            @ModelAttribute ArticleDto.ApproveRequest approveRequest, @AuthenticationPrincipal User adminUser) {
        return ResponseEntity.ok(articleService.approveArticle(id, approveRequest, adminUser));
    }

    @PostMapping(path = "/reject/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticleDto.Detailed> rejectArticle(
            @PathVariable UUID id, @ModelAttribute ArticleDto.RejectionRequest rejectionRequest,
            @AuthenticationPrincipal User adminUser) {
        return ResponseEntity.ok(articleService.rejectArticle(id, rejectionRequest, adminUser));
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<?> publishArticle(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.publishArticle(id, userDetails.getId()));
    }

    @PostMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishArticle(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.unpublishArticle(id, userDetails.getId()));
    }

    @PostMapping("/convert-draft/{id}")
    public ResponseEntity<?> convertArticleToDraft(@PathVariable UUID id, @AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(articleService.draftArticle(id, userDetails));
    }

    @Transactional
    @GetMapping("/export")
    public void exportArticlesToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=articles.csv");

        PrintWriter writer = response.getWriter();
        writer.println("id,title,content,status,isPublished,publishedAt,createdAt,updatedAt,authorId,categoryId");

        try (Stream<Article> articleStream = articleRepository.streamAll()) {
            articleStream.forEach(article -> writer.printf("%s,%s,%s,%s,%b,%s,%s,%s,%s,%s\n",
                    article.getId(),
                    article.getTitle(),
                    article.getContent().replaceAll("\n", " ").replaceAll(",", " "),

                    article.getStatus(),
                    article.getIsPublished(),
                    article.getPublishedAt(),
                    article.getCreatedAt(),
                    article.getUpdatedAt(),
                    article.getUser() != null ? article.getUser().getId() : "",
                    article.getCategory() != null ? article.getCategory().getId() : ""));
        }

        writer.flush();
    }
}
