package com.example.internintelligence_movieapidevelopment.client;

public interface TmdbTemplate {
    String GET_POPULAR = "/movie/popular";
    String GET_TOP_RATED = "/movie/top_rated";
    String UPCOMING = "/movie/upcoming";
    String GET_CREDITS = "/movie/{tmdbId}/credits";
    String GET_REVIEWS = "/movie/{tmdbId}/reviews";
    String GET_MOVIE_DETAILS = "/movie/{tmdbId}";
}
