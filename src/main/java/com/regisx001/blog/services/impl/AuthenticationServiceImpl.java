package com.regisx001.blog.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.dto.requests.VerifyUserRequest;
import com.regisx001.blog.domain.entities.Role;
import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.repositories.RoleRepository;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.AuthenticationService;
import com.regisx001.blog.services.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    @Override
    public User register(UserDto.RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new IllegalArgumentException(
                    "An account with this email : " + registerRequest.email() + " already exist.");
        }

        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new IllegalArgumentException(
                    "An account with this username : " + registerRequest.username() + " already exist.");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.password());

        User user = User.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .password(hashedPassword).build();

        Role userRole = roleRepository.findByName(RoleType.ROLE_USER);
        user.setRoles(Set.of(userRole));

        user.setVerificationCode(generateVerificationCode());
        user.setAvatar("https://api.dicebear.com/9.x/avataaars/svg?seed=" + user.getUsername());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        // sendVerificationEmail(user);
        return userRepository.save(user);
    }

    @Override
    public User authenticate(UserDto.LoginRequest loginUserRequest) {
        // if (loginUserRequest.getEmail() == null ||
        // loginUserRequest.getEmail().trim().isEmpty()) {

        // if (loginUserRequest.getUsername() == null ||
        // loginUserRequest.getUsername().trim().isEmpty()) {
        // throw new IllegalArgumentException("You need to login with either the
        // username or email");
        // }
        // }

        Optional<User> optionalUser;

        if (loginUserRequest.email() != null) {
            optionalUser = userRepository.findByEmail(loginUserRequest.email());
        } else if (loginUserRequest.username() != null) {
            optionalUser = userRepository.findByUsername(loginUserRequest.username());
        } else {
            throw new IllegalArgumentException("Email or username must be provided.");
        }

        User user = optionalUser.orElseThrow(() -> new RuntimeException("Account Not found"));

        // if (!user.isEnabled()) {
        // throw new IllegalStateException("Account not verified");
        // }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),
                        loginUserRequest.password()));
        return user;
    }

    @Override
    public void verifyUser(VerifyUserRequest verifyUserRequest) {
        Optional<User> userToVerify = userRepository.findByEmail(verifyUserRequest.getEmail());
        if (!userToVerify.isPresent()) {
            throw new UsernameNotFoundException("Account not found");
        }

        User user = userToVerify.get();
        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has been expired");
        }

        if (user.getVerificationCode().equals(verifyUserRequest.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);

            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid verification code");
        }

    }

    @Override
    public void resendVerificationCode(String email) {

        Optional<User> userToVerify = userRepository.findByEmail(email);
        if (!userToVerify.isPresent()) {
            throw new UsernameNotFoundException("Account not found");
        }

        User user = userToVerify.get();
        if (user.isEnabled()) {
            throw new RuntimeException("Account is already verified");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));

        userRepository.save(user);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    @Async("taskExecutor")
    @Override
    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f7fc; padding: 30px;\">"
                + "<div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); padding: 20px;\">"
                + "<h2 style=\"color: #4CAF50; text-align: center; font-size: 26px;\">Welcome to Our App!</h2>"
                + "<p style=\"font-size: 16px; color: #555; text-align: center; margin-bottom: 20px;\">We are excited to have you on board. Please use the verification code below to verify your account:</p>"
                + "<div style=\"background-color: #f9f9f9; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.05); text-align: center;\">"
                + "<h3 style=\"color: #333; font-size: 20px;\">Your Verification Code</h3>"
                + "<p style=\"font-size: 24px; font-weight: bold; color: #007bff; padding: 10px 20px; background-color: #e0f7fa; border-radius: 5px;\">"
                + verificationCode
                + "</p>"
                + "</div>"
                + "<p style=\"font-size: 14px; color: #888; text-align: center; margin-top: 20px;\">If you did not request this, please ignore this email.</p>"
                + "<p style=\"font-size: 14px; color: #888; text-align: center;\">Best regards,<br>Our App Team</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

}
