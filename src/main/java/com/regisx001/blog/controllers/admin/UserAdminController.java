package com.regisx001.blog.controllers.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.dto.responses.SuccessResponse;
import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class UserAdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<UserDto.Detailed>> getAllUsers(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled, Pageable pageable) {
        RoleType roleType = null;
        if (role != null) {
            try {
                roleType = RoleType.valueOf(role); // Convert String to RoleType enum
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid role: " + role);
            }
        }
        Page<UserDto.Detailed> users = userService.getAllUsersByFilters(searchTerm, roleType, enabled, pageable);
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

    @PostMapping(path = "/disable-user/{id}")
    public ResponseEntity<SuccessResponse> disableUserAccount(@PathVariable UUID id) {
        userService.changeEnable(id, false);
        SuccessResponse response = SuccessResponse.builder().statusCode(200)
                .message("User disabled successfully").build();
        return new ResponseEntity<SuccessResponse>(response, null, 200);
    }

    @PostMapping(path = "/enable-user/{id}")
    public ResponseEntity<SuccessResponse> enableUserAccount(@PathVariable UUID id) {
        userService.changeEnable(id, true);

        SuccessResponse response = SuccessResponse.builder().statusCode(200)
                .message("User enabled successfully").build();
        return new ResponseEntity<SuccessResponse>(response, null, 200);
    }

    @Transactional
    @GetMapping("/export")
    public void exportUsersToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");

        PrintWriter writer = response.getWriter();
        writer.println("id,username,email,enabled,createdAt,updatedAt");

        try (Stream<User> userStream = userRepository.streamAll()) {
            userStream.forEach(user -> writer.printf("%s,%s,%s,%b,%s,%s\n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.isEnabled(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()));
        }
        writer.flush();
    }

}
