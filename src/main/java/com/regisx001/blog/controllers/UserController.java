package com.regisx001.blog.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.dto.UserDtoRef;
import com.regisx001.blog.domain.dto.requests.UpdateUserRequest;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.UserMapper;
import com.regisx001.blog.mappers.UserMapperRef;
import com.regisx001.blog.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserMapperRef userMapperRef;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDtoRef.Detailed>> getAllUsers(Pageable pageable) {
        Page<UserDtoRef.Detailed> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    // @PostMapping(path = "/set-admin")
    // public ResponseEntity<SuccessResponse> setUserToAdmin(
    // @RequestBody UpdateUserRoleRequest updateUserRoleRequest) {

    // userService.upgradeUserAdmin(updateUserRoleRequest.getUserId(),
    // updateUserRoleRequest.getRoleNames());
    // SuccessResponse response = SuccessResponse.builder().statusCode(200)
    // .message("User upgraded to admin successfully").build();
    // return new ResponseEntity<SuccessResponse>(response, null, 200);
    // }

    @PostMapping(path = "/{userId}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile avatarFile) {
        User updatedUser = userService.uploadAvatar(userId, avatarFile);
        return ResponseEntity.ok(userMapperRef.toDetailedDto(updatedUser));
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest updateUserRequest) {

        User updatedUser = userService.updateUser(userId, updateUserRequest);

        return ResponseEntity.ok(userMapperRef.toDetailedDto(updatedUser));
    }

}
