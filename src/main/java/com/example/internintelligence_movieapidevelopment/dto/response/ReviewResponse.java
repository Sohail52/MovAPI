package com.example.internintelligence_movieapidevelopment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String author;
    private String content;
    private String url;
    private String created_at;
    private String updated_at;
}
