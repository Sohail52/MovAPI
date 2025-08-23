package com.example.internintelligence_movieapidevelopment.mapper;

import com.example.internintelligence_movieapidevelopment.dao.entity.Genre;
import com.example.internintelligence_movieapidevelopment.dto.request.GenreRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.GenreResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    Genre toEntity(GenreRequestDto requestDto);

    GenreResponseDto toDto(Genre genre);

    void mapForUpdate(@MappingTarget Genre genre, GenreRequestDto requestDto);
}
