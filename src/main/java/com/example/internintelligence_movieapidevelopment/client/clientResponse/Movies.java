package com.example.internintelligence_movieapidevelopment.client.clientResponse;

import com.example.internintelligence_movieapidevelopment.dto.response.MovieResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movies implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int page;
    private List<MovieResponseDto> results;
    private int totalPages;
    private int totalResults;
}
