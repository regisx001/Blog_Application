package com.regisx001.blog.controllers.admin;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.dto.responses.SuccessResponse;
import com.regisx001.blog.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto.Detailed>> getAllUsers(Pageable pageable) {
        Page<UserDto.Detailed> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @PostMapping(path = "/set-admin/{id}")
    public ResponseEntity<SuccessResponse> setUserToAdmin(@PathVariable UUID id) {
        Set<String> roles = Set.of("ROLE_ADMIN");
        userService.changeUserRoles(id, roles);
        SuccessResponse response = SuccessResponse.builder().statusCode(200)
                .message("User upgraded to ADMIN successfully").build();
        return new ResponseEntity<SuccessResponse>(response, null, 200);
    }

    @PostMapping(path = "/set-user/{id}")
    public ResponseEntity<SuccessResponse> setToUser(@PathVariable UUID id) {
        Set<String> roles = Set.of("ROLE_USER");
        userService.changeUserRoles(id, roles);
        SuccessResponse response = SuccessResponse.builder().statusCode(200)
                .message("User upgraded to USER successfully").build();
        return new ResponseEntity<SuccessResponse>(response, null, 200);
    }
}
