package com.example.internintelligence_movieapidevelopment.controller;

import com.example.internintelligence_movieapidevelopment.dto.request.MovieFilterDto;
import com.example.internintelligence_movieapidevelopment.dto.request.MovieRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.MovieResponseDto;
import com.example.internintelligence_movieapidevelopment.dto.response.PersonResponseDto;
import com.example.internintelligence_movieapidevelopment.dto.response.ReviewResponse;
import com.example.internintelligence_movieapidevelopment.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public MovieResponseDto getMovieByID(@PathVariable Integer id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/get-all")
    public Page<MovieResponseDto> getMovies(@PageableDefault Pageable pageable, MovieFilterDto movieFilterDto) {
        return movieService.getMovies(pageable, movieFilterDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public MovieResponseDto addMovie(@Validated @RequestBody MovieRequestDto requestDto) {
        return movieService.addMovie(requestDto);
    }

    @PutMapping("/edit/{id}")
    public MovieResponseDto editMovie(
            @PathVariable Integer id,
            @Validated @RequestBody MovieRequestDto movieRequestDto
    ) {
        return movieService.editMovie(id, movieRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
    }

    @GetMapping("{id}/reviews")
    public List<ReviewResponse> getReview(@PathVariable Integer id, @RequestParam int page) {
        return movieService.getReviews(id, page);
    }

    @GetMapping("/{id}/cast")
    public List<PersonResponseDto> getCast(@PathVariable Integer id) {
        return movieService.getCast(id);
    }


    @GetMapping("/popular")
    public List<MovieResponseDto> getPopularMovies(@RequestParam int page) {
        return movieService.getPopularMoviesAsList(page);
    }


    @GetMapping("/top_rated")
    public List<MovieResponseDto> getTopRatedMovies(@RequestParam int page) {
        return movieService.getTopRatedMoviesAsList(page);
    }


    @GetMapping("/upcoming")
    public List<MovieResponseDto> getUpcomingMovies(@RequestParam int page) {
        return movieService.getUpcomingMoviesAsList(page);
    }


}
