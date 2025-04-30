package com.regisx001.blog.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.dto.requests.LoginUserRequest;
import com.regisx001.blog.domain.dto.requests.RegisterUserRequest;
import com.regisx001.blog.domain.dto.requests.VerifyUserRequest;
import com.regisx001.blog.domain.dto.responses.LoginResponse;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.UserMapper;
import com.regisx001.blog.services.AuthenticationService;
import com.regisx001.blog.services.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping(path = "/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        User savedUser = authenticationService.register(userMapper.toEntity(registerUserRequest));
        return new ResponseEntity<>(userMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        User authenticateUser = authenticationService.authenticate(loginUserRequest);
        String jwtToken = jwtService.generateToken(authenticateUser);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expireIn(jwtService.getJwtExpiration()).build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserRequest verifyUserRequest) {
        try {
            authenticationService.verifyUser(verifyUserRequest);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
