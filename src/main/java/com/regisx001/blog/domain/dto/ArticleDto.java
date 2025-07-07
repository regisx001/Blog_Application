package com.regisx001.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.regisx001.blog.domain.entities.Enums.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private UUID id;
    private String title;
    private String content;
    private String featuredImage;
    private ArticleStatus status;
    private Boolean isPublished;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto user;
    private CategoryDto.Basic category;
    private List<TagDto> tags;
}
