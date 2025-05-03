package com.regisx001.blog.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.UserMapper;
import com.regisx001.blog.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
@EnableMethodSecurity
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.toDto(currentUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> allUsers() {
        List<UserDto> users = userService.allUsers().stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok(users);
    }

}
