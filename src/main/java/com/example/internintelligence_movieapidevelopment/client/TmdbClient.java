package com.example.internintelligence_movieapidevelopment.client;

import com.example.internintelligence_movieapidevelopment.client.clientResponse.*;
import com.example.internintelligence_movieapidevelopment.client.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tmdbClient", url = "${tmdb.url}", configuration = FeignConfig.class)
public interface TmdbClient {

    @GetMapping(TmdbTemplate.GET_POPULAR)
    Movies getPopularMovies(
            @RequestParam("language") String language,
            @RequestParam("page") int page
    );

    @GetMapping(TmdbTemplate.GET_TOP_RATED)
    Movies getTopRatedMovies(
            @RequestParam("language") String language,
            @RequestParam("page") int page
    );

    @GetMapping(TmdbTemplate.UPCOMING)
    Movies getUpcomingMovies(
            @RequestParam("language") String language,
            @RequestParam("page") int page
    );

    @GetMapping(TmdbTemplate.GET_CREDITS)
    Credits fetchMovieCredits(@PathVariable("tmdbId") Integer tmdbId);

    @GetMapping(TmdbTemplate.GET_REVIEWS)
    Reviews fetchMovieReviews(@PathVariable("tmdbId") Integer tmdbId,
                              @RequestParam("page") int page);

    @GetMapping(TmdbTemplate.GET_MOVIE_DETAILS)
    com.example.internintelligence_movieapidevelopment.dto.response.MovieResponseDto fetchMovieDetails(
            @PathVariable("tmdbId") Integer tmdbId,
            @RequestParam("language") String language
    );

}
