package com.regisx001.blog.domain;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.repositories.RoleRepository;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.AuthenticationService;
import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Override
    public void run(String... args) {

        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    // new Role(null, RoleType.ROLE_USER),
                    Role.builder().name(RoleType.ROLE_USER).build(),
                    Role.builder().name(RoleType.ROLE_ADMIN).build()));
        }

        if (userRepository.count() == 0) {
            UserDto.RegisterRequest registerRequest = new UserDto.RegisterRequest("ezzoubair",
                    "zarqi.ezzoubair@gmail.com ", "password");
            User user = authenticationService.register(registerRequest);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            user.setEnabled(true);
            // ✅ ADD ADMIN ROLE
            // Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN);
            // // .orElseThrow(() -> new RuntimeException("Admin role not found"));

            // user.getRoles().add(adminRole);

            // Save the updated user
            userRepository.save(user);
        }
    }
}