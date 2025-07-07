package com.regisx001.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDtoRef {

        // ============= BASIC USER DTO =============
        public record Basic(
                        UUID id,
                        String username,
                        String avatar) {
        }

        // ============= DETAILED USER DTO =============
        public record Detailed(
                        UUID id,
                        String username,
                        String email,
                        String avatar,
                        boolean enabled,
                        Set<String> roles,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        }

        // ============= PUBLIC PROFILE =============
        public record Profile(
                        UUID id,
                        String username,
                        String email,
                        String avatar,
                        LocalDateTime createdAt) {
        }

        // ============= USER WITH ARTICLE COUNT =============
        public record WithCount(
                        UUID id,
                        String username,
                        String avatar,
                        Integer articlesCount,
                        LocalDateTime createdAt) {
        }

        // ============= AUTHENTICATION RESPONSE =============
        public record AuthResponse(
                        UUID id,
                        String username,
                        String email,
                        String avatar,
                        Set<String> roles,
                        String token) {
        }

        // ============= REGISTRATION REQUEST =============
        public record RegisterRequest(
                        @NotBlank(message = "Username is required") @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username,

                        @NotBlank(message = "Email is required") @Email(message = "Please provide a valid email address") String email,

                        @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
        }

        // ============= LOGIN REQUEST =============
        public record LoginRequest(
                        @NotBlank(message = "Username or email is required") String username,
                        String email,

                        @NotBlank(message = "Password is required") String password) {
        }

        // ============= UPDATE REQUEST =============
        public record UpdateRequest(
                        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username,

                        @Email(message = "Please provide a valid email address") String email,

                        @Size(max = 255, message = "Avatar URL cannot exceed 255 characters") String avatar) {
        }

        // ============= ADMIN VIEW =============
        public record Admin(
                        UUID id,
                        String username,
                        String email,
                        String avatar,
                        boolean enabled,
                        Set<String> roles,
                        Integer articlesCount,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        }

        // ============= USER OPTION FOR DROPDOWNS =============
        public record Option(
                        UUID id,
                        String username) {
        }
}