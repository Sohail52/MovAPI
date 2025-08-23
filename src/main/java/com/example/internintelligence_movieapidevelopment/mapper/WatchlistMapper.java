package com.example.internintelligence_movieapidevelopment.mapper;

import com.example.internintelligence_movieapidevelopment.dao.entity.Watchlist;
import com.example.internintelligence_movieapidevelopment.dto.response.WatchlistResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WatchlistMapper {
    
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieName")
    WatchlistResponseDto toDto(Watchlist watchlist);
    
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "movieId", target = "movie.id")
    Watchlist toEntity(WatchlistResponseDto dto);
}
