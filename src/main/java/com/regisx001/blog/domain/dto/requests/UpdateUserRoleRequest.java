package com.regisx001.blog.domain.dto.requests;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    private UUID userId;
    private Set<String> roleNames;
}