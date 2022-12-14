package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.security.userDetails.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserDto userDto);
    UserDto toUser(CustomUserDetails customUserDetails);
    UserDto toDto(User user);
}
