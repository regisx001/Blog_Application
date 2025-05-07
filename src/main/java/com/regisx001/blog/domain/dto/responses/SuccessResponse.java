package com.regisx001.blog.domain.dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponse {
    private String message;
    private int statusCode;
}
