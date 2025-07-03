package com.regisx001.blog.domain.dto.requests;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String title;
    private String description;
    private String image;
}
