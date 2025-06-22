package com.regisx001.blog.services;

import com.regisx001.blog.domain.dto.UserDto;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public Page<UserDto> getAllUsers(Pageable pageable);

    public void upgradeUserAdmin(UUID userid, Set<String> rolesName);

    UserDto uploadAvatar(UUID userId, MultipartFile file);

}
