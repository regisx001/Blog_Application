package com.regisx001.blog.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;
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
