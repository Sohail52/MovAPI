package com.example.internintelligence_movieapidevelopment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreRequestDto {
    @NotBlank(message = "Genre name is required")
    private String name;
    @NotBlank(message = "Genre description is required")
    @Size(min = 2, max = 200, message = "Description must be between 2 and 200 characters.")
    private String description;
}
