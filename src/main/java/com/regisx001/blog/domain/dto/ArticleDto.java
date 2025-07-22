package com.regisx001.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.regisx001.blog.domain.entities.Enums.ArticleStatus;
import com.regisx001.blog.validation.ValidImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ArticleDto {

        // ============= BASIC ARTICLE DTO =============
        public record Basic(
                        UUID id,
                        String title,
                        String featuredImage,
                        CategoryDto.Basic category,
                        UserDto.Basic user,
                        LocalDateTime createdAt) {
        }

        // ============= DETAILED ARTICLE DTO =============
        public record Detailed(
                        UUID id,
                        String title,
                        String content,
                        String featuredImage,
                        ArticleStatus status,
                        String feedback,
                        Boolean isPublished,
                        LocalDateTime publishedAt,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt,
                        LocalDateTime rejectedAt,
                        String rejectedBy,
                        LocalDateTime approvedAt,
                        String approvedBy,
                        UserDto.Basic author,
                        CategoryDto.Basic category,
                        List<String> tags) {
        }

        // ============= ARTICLE SUMMARY FOR LISTS =============
        public record Summary(
                        UUID id,
                        String title,
                        String excerpt, // First 200 chars of content
                        String featuredImage,
                        ArticleStatus status,
                        Boolean isPublished,
                        CategoryDto.Basic category,
                        UserDto.Basic user,
                        LocalDateTime createdAt) {
        }

        // ============= CREATE REQUEST =============
        public record CreateRequest(
                        @NotBlank(message = "Title is required") @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters") String title,

                        @NotBlank(message = "Content is required") @Size(min = 5, message = "Content must be at least 5 characters") String content,

                        String category,

                        Boolean draft,

                        List<String> tags,

                        // ArticleStatus status,

                        Boolean isPublished,

                        @ValidImage(message = "Invalid image file") MultipartFile featuredImage

        ) {
        }

        // ============= UPDATE REQUEST =============
        public record UpdateRequest(
                        @NotBlank(message = "Title is required") @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters") String title,

                        @NotBlank(message = "Content is required") @Size(min = 50, message = "Content must be at least 50 characters") String content,

                        UUID categoryId,

                        List<String> tags,

                        @NotNull(message = "Status is required") ArticleStatus status,

                        Boolean isPublished,

                        @Size(max = 255, message = "Featured image URL cannot exceed 255 characters") String featuredImage) {
        }

        // ============= PUBLISH/UNPUBLISH REQUEST =============
        public record PublishRequest(
                        Boolean isPublished) {
        }

        // ============= DRAFT ARTICLE =============
        public record Draft(
                        UUID id,
                        String title,
                        String content,
                        String featuredImage,
                        CategoryDto.Basic category,
                        List<TagDto.Basic> tags,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        }

        // ============= ARTICLE OPTION FOR DROPDOWNS =============
        public record Option(
                        UUID id,
                        String title) {
        }

        public record DeleteInBatchRequest(
                        List<UUID> ids) {
        }

        public record ApproveRequest(
                        String feedback) {
        }

        public record RejectionRequest(
                        String feedback) {
        }
}