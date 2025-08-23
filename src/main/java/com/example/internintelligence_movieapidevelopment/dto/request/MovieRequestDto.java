package com.example.internintelligence_movieapidevelopment.dto.request;

import com.example.internintelligence_movieapidevelopment.validation.MinSize;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestDto {

    @NotNull(message = "TMDB id can't be null")
    private Integer tmdbId;

    @NotBlank(message = "Tittle can't be blank.")
    @Size(min = 2, max = 100, message = "Tittle must be between 2 and 100 characters.")
    private String title;

    @NotBlank(message = "Overview can't be blank")
    @Size(max = 500, message = "Overview cannot exceed 500 characters.")
    private String overview;

    @NotNull(message = "Release date is required")
    @PastOrPresent(message = "Released date can't be in the future")
    private LocalDate releaseDate;

    @NotNull(message = "Vote average can't be null")
    private Double voteAverage;

    @Min(30)
    @Max(240)
    @NotNull(message = "Runtime is required")
    private Integer runtime;

    @MinSize(value = 1, message = "The movie must have at least 1 genre.")
    private List<Long> genresId;
}
