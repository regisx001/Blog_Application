package com.regisx001.blog.services;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.dto.requests.LoginUserRequest;
import com.regisx001.blog.domain.dto.requests.VerifyUserRequest;
import com.regisx001.blog.domain.entities.User;

public interface AuthenticationService {
    public User register(UserDto.RegisterRequest registerRequest);

    public User authenticate(UserDto.LoginRequest loginUserRequest);

    public void verifyUser(VerifyUserRequest verifyUserRequest);

    public void resendVerificationCode(String email);

    public void sendVerificationEmail(User user);

}
