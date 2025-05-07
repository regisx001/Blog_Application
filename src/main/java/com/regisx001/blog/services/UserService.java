package com.regisx001.blog.services;

import com.regisx001.blog.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    public Page<UserDto> getAllUsers(Pageable pageable);
}
