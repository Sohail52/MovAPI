package com.example.internintelligence_movieapidevelopment.client.clientException;

import com.example.internintelligence_movieapidevelopment.exception.IllegalArgumentException;
import com.example.internintelligence_movieapidevelopment.exception.ResourceNotFoundException;
import com.example.internintelligence_movieapidevelopment.exception.TmdbServiceNotAvailableException;
import com.example.internintelligence_movieapidevelopment.exception.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class TmdbErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new IllegalArgumentException("Bad request to TMDB API: " + response.request().url());
            case 401 -> new UnauthorizedException("Unauthorized request to TMDB API: " + response.request().url());
            case 404 -> new ResourceNotFoundException("Resource not found at TMDB API: " + response.request().url());
            case 503 -> new TmdbServiceNotAvailableException("TMDB Api is unavailable.Try later.");
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }
}

