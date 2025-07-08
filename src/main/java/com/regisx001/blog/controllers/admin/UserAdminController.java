package com.regisx001.blog.controllers.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regisx001.blog.domain.dto.requests.UpdateUserRoleRequest;
import com.regisx001.blog.domain.dto.responses.SuccessResponse;
import com.regisx001.blog.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    // @PreAuthorize("hasRole('ADMIN')")
    // @GetMapping
    // public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
    // Page<UserDto> users = userService.getAllUsers(pageable);
    // return ResponseEntity.ok(users);
    // }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/set-admin")
    public ResponseEntity<SuccessResponse> setUserToAdmin(
            @RequestBody UpdateUserRoleRequest updateUserRoleRequest) {

        userService.upgradeUserAdmin(updateUserRoleRequest.getUserId(), updateUserRoleRequest.getRoleNames());
        SuccessResponse response = SuccessResponse.builder().statusCode(200)
                .message("User upgraded to admin successfully").build();
        return new ResponseEntity<SuccessResponse>(response, null, 200);
    }
}
