package com.regisx001.blog.services;

import java.util.UUID;

import com.regisx001.blog.domain.entities.User;

public interface UserService {

    User createUser(User user);

    User updateUser(UUID userId, User user);
}
