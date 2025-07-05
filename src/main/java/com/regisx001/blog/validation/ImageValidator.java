package com.regisx001.blog.validation;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp");

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        // Null or empty files are considered valid (optional field)
        if (file == null || file.isEmpty()) {
            return true;
        }

        // Check file size
        if (file.getSize() > MAX_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Image size must be less than 5MB")
                    .addConstraintViolation();
            return false;
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only JPEG, PNG, GIF and WebP images are allowed")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}