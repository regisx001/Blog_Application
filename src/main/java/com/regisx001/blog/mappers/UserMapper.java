package com.regisx001.blog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.regisx001.blog.domain.dto.CreateUserRequest;
import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(CreateUserRequest createUserRequest);
}
