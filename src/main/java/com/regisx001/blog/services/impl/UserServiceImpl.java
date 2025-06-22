package com.regisx001.blog.services.impl;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.Role;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.regisx001.blog.repositories.RoleRepository;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.StorageService;
import com.regisx001.blog.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StorageService storageService;
    private final UserMapper userMapper;

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public void upgradeUserAdmin(UUID userid, Set<String> roleNames) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UsernameNotFoundException("User with giving Id not found"));
        Set<Role> roles = roleRepository.findByNameIn(roleNames);

        if (roles.size() != roleNames.size()) {
            throw new IllegalArgumentException("Some roles are invalid");
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserDto uploadAvatar(UUID userId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String avatarFileName = storageService.store(file);
        user.setAvatar(avatarFileName);
        return userMapper.toDto(userRepository.save(user));
    }

}
