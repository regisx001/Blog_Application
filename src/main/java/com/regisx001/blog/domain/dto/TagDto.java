package com.regisx001.blog.domain.dto;

import lombok.Builder;
import lombok.Data;

// TODO: CHANGE DTO STRUCTURE TO RECORDS

@Data
@Builder
public class TagDto {
    // private UUID id;
    private String name;
    private String slug;
    // private String description;
    // private LocalDateTime createdAt;
}
