package com.regisx001.blog.domain.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyTokenRequest {

    @NotBlank(message = "Token is Required")
    private String token;
}
