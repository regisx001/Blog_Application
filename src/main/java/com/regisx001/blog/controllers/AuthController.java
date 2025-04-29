package com.regisx001.blog.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.AuthResponse;
import com.regisx001.blog.domain.dto.CreateUserRequest;
import com.regisx001.blog.domain.dto.LoginRequest;
import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.UserMapper;
import com.regisx001.blog.services.AuthenticationService;
import com.regisx001.blog.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(),
                loginRequest.getPassword());
        String tokenValue = authenticationService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expireIn(86400).build();

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody CreateUserRequest createUserRequest) {
        User userToCreate = userMapper.toEntity(createUserRequest);
        User savedUser = userService.createUser(userToCreate);

        return new ResponseEntity<>(userMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @PutMapping("/update/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable("user_id") UUID userId, @RequestBody UserDto userDto) {
        return ResponseEntity.ok("updated");
    }

}
