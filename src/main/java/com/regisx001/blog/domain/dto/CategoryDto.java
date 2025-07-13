package com.regisx001.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.regisx001.blog.validation.ValidImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDto {

        // ============= BASIC CATEGORY DTO =============
        public record Basic(
                        UUID id,
                        String title,
                        String description,
                        String image,
                        LocalDateTime createdAt) {
        }

        // ============= DETAILED CATEGORY DTO =============
        public record Detailed(
                        UUID id,
                        String title,
                        String description,
                        String image,
                        // List<ArticleDto.Basic> articles,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        }

        // ============= CREATE REQUEST (JSON) =============
        public record CreateRequest(
                        @NotBlank(message = "Title is required") @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters") String title,

                        @NotBlank(message = "Description is required") @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters") String description) {
        }

        // ============= CREATE REQUEST WITH IMAGE (MULTIPART) =============
        public record CreateWithImageRequest(
                        @NotBlank(message = "Title is required") @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters") String title,

                        @NotBlank(message = "Description is required") @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters") String description,

                        @ValidImage(message = "Invalid image file") MultipartFile image) {
        }

        // ============= UPDATE REQUEST =============
        public record UpdateRequest(
                        @NotBlank(message = "Title is required") @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters") String title,

                        @NotBlank(message = "Description is required") @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters") String description,

                        @Size(max = 255, message = "Image URL cannot exceed 255 characters") String image) {
        }

        // ============= OPTION FOR DROPDOWNS =============
        public record Option(
                        UUID id,
                        String title) {
        }
}