package com.regisx001.blog.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.regisx001.blog.domain.dto.requests.RegisterUserRequest;
import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.Role;
import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.domain.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToNames")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "avatarNameToUri")
    UserDto toDto(User user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapNamesToRoles")
    User toEntity(UserDto userDto);

    User toEntity(RegisterUserRequest registerUserRequest);

    @Named("mapRolesToNames")
    default Set<String> mapRolesToNames(Set<Role> roles) {
        if (roles == null)
            return Set.of();
        return roles.stream()
                .map(role -> role.getName().name()) // RoleType.ROLE_USER -> "ROLE_USER"
                .collect(Collectors.toSet());
    }

    @Named("mapNamesToRoles")
    default Set<Role> mapNamesToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty())
            return Set.of();
        return roleNames.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(RoleType.valueOf(roleName)); // "ROLE_USER" -> RoleType.ROLE_USER
                    return role;
                })
                .collect(Collectors.toSet());
    }

    @Named("avatarNameToUri")
    default String avatarNameToUri(String avatar) {
        if (avatar == null || avatar.isBlank()) {
            return null;
        }
        return "/uploads/" + avatar;
    }

}