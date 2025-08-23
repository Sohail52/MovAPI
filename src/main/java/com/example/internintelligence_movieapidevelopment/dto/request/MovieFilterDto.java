package com.example.internintelligence_movieapidevelopment.dto.request;

import lombok.Data;

@Data
public class MovieFilterDto {
    private String title;
    private String genre;
    private Integer year;
    private Double minRating;
    private Double maxRating;
}
