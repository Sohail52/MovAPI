package com.example.internintelligence_movieapidevelopment.mapper;

import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import com.example.internintelligence_movieapidevelopment.dto.request.MovieRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.MovieResponseDto;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponseDto toDto(Movie movie);

    Movie toEntity(MovieRequestDto movieRequestDto);

    void mapForUpdate(@MappingTarget Movie movie, MovieRequestDto movieRequestDto);
}
