package com.example.internintelligence_movieapidevelopment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistResponseDto {
    private Integer userId;
    private Integer movieId;
    private String movieName;
    private LocalDate addAt;
}
