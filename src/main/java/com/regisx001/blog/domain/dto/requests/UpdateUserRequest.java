package com.regisx001.blog.domain.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String username;
}
