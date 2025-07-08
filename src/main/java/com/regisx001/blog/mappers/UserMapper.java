package com.regisx001.blog.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;

import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.Role;
import com.regisx001.blog.domain.entities.RoleType;
import com.regisx001.blog.domain.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Value("${app.base-url:http://localhost:8080}")
    protected String baseUrl;

    // ============= TO DTO MAPPINGS =============

    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "avatarNameToFullUri")
    public abstract UserDto.Basic toBasicDto(User user);

    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "avatarNameToFullUri")
    public abstract UserDto.Profile toProfileDto(User user);

    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "avatarNameToFullUri")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToNames")
    public abstract UserDto.Detailed toDetailedDto(User user);

    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "avatarNameToFullUri")
    @Mapping(target = "articlesCount", expression = "java(calculateArticlesCount(user))")
    public abstract UserDto.WithCount toWithCountDto(User user);

    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "avatarNameToFullUri")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToNames")
    @Mapping(target = "articlesCount", expression = "java(calculateArticlesCount(user))")
    public abstract UserDto.Admin toAdminDto(User user);

    public abstract UserDto.Option toOptionDto(User user);

    // ============= TO ENTITY MAPPINGS =============

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // handled separately for security
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "roles", ignore = true) // handled separately in service
    // @Mapping(target = "articles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "avatar", ignore = true) // handled separately if multipart
    public abstract User toEntity(UserDto.RegisterRequest request);

    // ============= HELPER METHODS =============

    @Named("avatarNameToFullUri")
    protected String avatarNameToFullUri(String avatar) {
        if (avatar == null || avatar.isBlank()) {
            return "https://gravatar.com/avatar/4c3b0ab99ac413643387fc940a440210?s=400&d=retro&r=x"; // default avatar
        }

        // If it's already a full URL, return as is
        if (avatar.startsWith("http://") || avatar.startsWith("https://")) {
            return avatar;
        }

        // Remove leading slash if present to avoid double slashes
        String cleanAvatar = avatar.startsWith("/") ? avatar.substring(1) : avatar;
        return baseUrl + "/uploads/avatars/" + cleanAvatar;
    }

    @Named("mapRolesToNames")
    protected Set<String> mapRolesToNames(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }
        return roles.stream()
                .map(role -> role.getName().name()) // RoleType.ROLE_USER -> "ROLE_USER"
                .collect(Collectors.toSet());
    }

    @Named("mapNamesToRoles")
    protected Set<Role> mapNamesToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Set.of();
        }
        return roleNames.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(RoleType.valueOf(roleName)); // "ROLE_USER" -> RoleType.ROLE_USER
                    return role;
                })
                .collect(Collectors.toSet());
    }

    protected Integer calculateArticlesCount(User user) {
        // return user.getArticles() != null ? user.getArticles().size() : 0;
        return 0;
    }
}