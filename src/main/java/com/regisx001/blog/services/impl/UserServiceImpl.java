package com.regisx001.blog.services.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> allUsers() {
        log.error("This is something goes here print hello world");
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

}
