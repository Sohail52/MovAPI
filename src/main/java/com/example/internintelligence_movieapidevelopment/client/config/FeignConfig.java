package com.example.internintelligence_movieapidevelopment.client.config;

import com.example.internintelligence_movieapidevelopment.client.clientException.TmdbErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new TmdbErrorDecoder();
    }

    @Value("${tmdb.api-key}")
    private String tmdbApiKey;

    @Bean
    public RequestInterceptor tmdbAuthInterceptor() {
        return template -> {
            if (tmdbApiKey != null && !tmdbApiKey.isBlank()) {
                // Use TMDB v4 auth via Bearer token
                template.header("Authorization", "Bearer " + tmdbApiKey);
            }
        };
    }
}
