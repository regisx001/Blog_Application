package com.regisx001.blog.domain.dto.requests;

import java.util.List;

import com.regisx001.blog.domain.entities.Enums.ArticleStatus;

import lombok.Data;

@Data
public class CreateArticleRequest {
    private String title;
    private String content;
    private String featuredImage;
    private ArticleStatus status;
    private Boolean isPublished;
    private String category;
    private List<String> tags;
}
