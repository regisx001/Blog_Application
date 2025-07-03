package com.regisx001.blog.domain.dto.requests;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private UUID id;
    private String title;
    private String description;
    private String image;
}
