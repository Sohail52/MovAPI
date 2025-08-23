package com.example.internintelligence_movieapidevelopment.mapper;

import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import com.example.internintelligence_movieapidevelopment.dto.request.UserRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto requestDto);
    UserResponseDto toDto(User user);
}
