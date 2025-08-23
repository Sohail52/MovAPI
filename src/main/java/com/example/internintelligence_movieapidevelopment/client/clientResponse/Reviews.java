package com.example.internintelligence_movieapidevelopment.client.clientResponse;

import com.example.internintelligence_movieapidevelopment.dto.response.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reviews {
    private int page;
    private List<ReviewResponse> results;
}
