package com.regisx001.blog.domain.dto.requests;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.regisx001.blog.validation.ValidImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @ValidImage(message = "Invalid image file")
    private MultipartFile image;

    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }
}
