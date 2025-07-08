package com.regisx001.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagDto {

    // ============= BASIC TAG DTO =============
    public record Basic(
            String name) {
    }

    // ============= DETAILED TAG DTO =============
    public record Detailed(
            UUID id,
            String name,
            LocalDateTime createdAt) {
    }

    // ============= TAG WITH ARTICLE COUNT =============
    public record WithCount(
            UUID id,
            String name,
            Integer articlesCount) {
    }

    // ============= CREATE REQUEST =============
    public record CreateRequest(
            @NotBlank(message = "Tag name is required") @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters") String name) {
    }

    // ============= TAG OPTION FOR DROPDOWNS =============
    public record Option(
            UUID id,
            String name) {
    }
}