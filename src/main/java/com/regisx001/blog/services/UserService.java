package com.regisx001.blog.services;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.dto.requests.UpdateUserRequest;
import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.domain.entities.User;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public Page<UserDto.Detailed> getAllUsers(Pageable pageable);

    public Page<UserDto.Detailed> getAllUsersByFilters(String searchTermsn, RoleType role, Boolean enabled,
            Pageable pageable);

    User findByUsername(String username);

    public void changeUserRoles(UUID userid, Set<String> rolesName);

    User uploadAvatar(UUID userId, MultipartFile file);

    User updateUser(UUID userId, UpdateUserRequest updateUserRequest);

    void changeEnable(UUID userId, boolean enable);

}
