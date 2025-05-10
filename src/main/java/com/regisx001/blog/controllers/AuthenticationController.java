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
import com.regisx001.blog.domain.dto.requests.TokenRefreshRequest;
import com.regisx001.blog.domain.dto.requests.VerifyUserRequest;
import com.regisx001.blog.domain.dto.responses.LoginResponse;
import com.regisx001.blog.domain.dto.responses.SuccessResponse;
import com.regisx001.blog.domain.dto.responses.TokenResponse;
import com.regisx001.blog.domain.entities.RefreshToken;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.UserMapper;
import com.regisx001.blog.services.AuthenticationService;
import com.regisx001.blog.services.JwtService;
import com.regisx001.blog.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping(path = "/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        User savedUser = authenticationService.register(userMapper.toEntity(registerUserRequest));
        authenticationService.sendVerificationEmail(savedUser);
        return new ResponseEntity<>(userMapper.toDto(savedUser), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        User authenticateUser = authenticationService.authenticate(loginUserRequest);
        String jwtToken = jwtService.generateToken(authenticateUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticateUser.getId());
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .expireIn(jwtService.getJwtExpiration()).build();

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserRequest verifyUserRequest) {
        try {
            authenticationService.verifyUser(verifyUserRequest);
            SuccessResponse response = SuccessResponse.builder().statusCode(200)
                    .message("Account verified successfully").build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            SuccessResponse response = SuccessResponse.builder().statusCode(200)
                    .message("Verification code sent").build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    TokenResponse response = TokenResponse.builder()
                            .accessToken(token)
                            .build();
                    return ResponseEntity
                            .ok(response);
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

}
