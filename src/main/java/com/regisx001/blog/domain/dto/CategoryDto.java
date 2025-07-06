package com.regisx001.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDto {

    // ============= BASIC CATEGORY DTO =============
    public record Basic(
            String title,
            String image) {
    }

    // ============= DETAILED CATEGORY DTO =============
    public record Detailed(
            UUID id,
            String title,
            String description,

            String image,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }

    // ============= CATEGORY WITH ARTICLE COUNT =============
    public record WithCount(
            UUID id,
            String title,

            String image,
            Integer articlesCount) {
    }

    // ============= CATEGORY FOR API RESPONSES =============
    public record Response(
            UUID id,
            String title,
            String description,

            String image,
            Integer articlesCount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }

    // ============= CATEGORY FOR CREATION REQUESTS =============
    public record CreateRequest(
            @NotBlank(message = "Title is required") @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters") String title,

            @NotBlank(message = "Description is required") @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") String description

    // Note: image is handled separately in multipart requests
    // slug is auto-generated from title
    ) {
    }

    // ============= CATEGORY FOR UPDATE REQUESTS =============
    public record UpdateRequest(
            @NotBlank(message = "Title is required") @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters") String title,

            @NotBlank(message = "Description is required") @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters") String description,

            @Size(max = 255, message = "Image URL cannot exceed 255 characters") String image) {
    }

    // ============= CATEGORY SUMMARY FOR LISTS =============
    public record Summary(
            UUID id,
            String title,

            String image,
            Integer articlesCount,
            LocalDateTime createdAt) {
    }

    // ============= CATEGORY FOR DROPDOWNS/SELECTS =============
    public record Option(
            UUID id,
            String title,
            String slug) {
    }
}