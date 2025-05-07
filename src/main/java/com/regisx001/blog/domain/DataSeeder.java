package com.regisx001.blog.domain;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.repositories.RoleRepository;
import com.regisx001.blog.domain.entities.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    // new Role(null, RoleType.ROLE_USER),
                    Role.builder().name(RoleType.ROLE_USER).build(),
                    Role.builder().name(RoleType.ROLE_ADMIN).build()));
        }
    }
}