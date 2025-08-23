package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.client.TmdbClient;
import com.example.internintelligence_movieapidevelopment.client.clientResponse.Credits;
import com.example.internintelligence_movieapidevelopment.client.clientResponse.Movies;
import com.example.internintelligence_movieapidevelopment.client.clientResponse.Reviews;
import com.example.internintelligence_movieapidevelopment.dao.entity.Genre;
import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import com.example.internintelligence_movieapidevelopment.dao.repository.MovieRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import com.example.internintelligence_movieapidevelopment.dto.request.MovieFilterDto;
import com.example.internintelligence_movieapidevelopment.dto.request.MovieRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.MovieResponseDto;
import com.example.internintelligence_movieapidevelopment.dto.response.PersonResponseDto;
import com.example.internintelligence_movieapidevelopment.dto.response.ReviewResponse;
import com.example.internintelligence_movieapidevelopment.exception.ResourceNotFoundException;
import com.example.internintelligence_movieapidevelopment.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final TmdbClient tmdbClient;
    private final MovieMapper movieMapper;
    private final MovieRepository movieRepository;
    
    @Value("${tmdb.default-language}")
    private String defaultLanguage;

    public Page<MovieResponseDto> getMovies(Pageable pageable, MovieFilterDto movieFilterDto) {
        // Create specifications based on filter criteria
        Specification<Movie> spec = Specification.where(null);
        
        if (movieFilterDto != null) {
            if (movieFilterDto.getTitle() != null && !movieFilterDto.getTitle().isEmpty()) {
                spec = spec.and((root, query, cb) -> 
                    cb.like(cb.lower(root.get("title")), "%" + movieFilterDto.getTitle().toLowerCase() + "%"));
            }
            
            if (movieFilterDto.getGenre() != null && !movieFilterDto.getGenre().isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    Join<Movie, Genre> genreJoin = root.join("genres");
                    return cb.equal(cb.lower(genreJoin.get("name")), movieFilterDto.getGenre().toLowerCase());
                });
            }
            
            if (movieFilterDto.getYear() != null) {
                spec = spec.and((root, query, cb) -> {
                    Expression<Integer> year = cb.function("YEAR", Integer.class, root.get("releaseDate"));
                    return cb.equal(year, movieFilterDto.getYear());
                });
            }
            
            if (movieFilterDto.getMinRating() != null) {
                spec = spec.and((root, query, cb) -> 
                    cb.greaterThanOrEqualTo(root.get("voteAverage"), movieFilterDto.getMinRating()));
            }
            
            if (movieFilterDto.getMaxRating() != null) {
                spec = spec.and((root, query, cb) -> 
                    cb.lessThanOrEqualTo(root.get("voteAverage"), movieFilterDto.getMaxRating()));
            }
        }
        
        Page<Movie> movies = movieRepository.findAll(spec, pageable);
        return movies.map(movieMapper::toDto);
    }

    @Cacheable(value = "popularMovies", key = "#page")
    public Movies getPopularMovies(int page) {
        return tmdbClient.getPopularMovies(defaultLanguage, page);
    }

    @Cacheable(value = "topRatedMovies", key = "#page")
    public Movies getTopRatedMovies(int page) {
        return tmdbClient.getTopRatedMovies(defaultLanguage, page);
    }

    @Cacheable(value = "upcomingMovies", key = "#page")
    public Movies getUpcomingMovies(int page) {
        return tmdbClient.getUpcomingMovies(defaultLanguage, page);
    }

    @Cacheable(value = "movieReviews", key = "#movieId + '-' + #page")
    public List<ReviewResponse> getMovieReviews(Integer movieId, int page) {
        Reviews reviews = tmdbClient.fetchMovieReviews(movieId, page);
        return reviews != null ? reviews.getResults() : List.of();
    }

    // Additional methods needed by MovieController
    @Cacheable(value = "movies", key = "#id")
    public MovieResponseDto getMovieById(Integer id) {
        var movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return movieMapper.toDto(movie);
    }

    public MovieResponseDto addMovie(MovieRequestDto requestDto) {
        if (movieRepository.existsByTmdbId(requestDto.getTmdbId())) {
            throw new IllegalArgumentException("Movie with TMDB ID " + requestDto.getTmdbId() + " already exists");
        }
        
        Movie movie = movieMapper.toEntity(requestDto);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    public MovieResponseDto editMovie(Integer id, MovieRequestDto requestDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        
        movieMapper.mapForUpdate(movie, requestDto);
        Movie updatedMovie = movieRepository.save(movie);
        return movieMapper.toDto(updatedMovie);
    }

    public void deleteMovie(Integer id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    public List<ReviewResponse> getReviews(Integer id, int page) {
        return getMovieReviews(id, page);
    }

    @Cacheable(value = "movieCast", key = "#id")
    public List<PersonResponseDto> getCast(Integer id) {
        Credits credits = tmdbClient.fetchMovieCredits(id);
        return credits != null && credits.getCast() != null ? credits.getCast() : List.of();
    }

    public List<MovieResponseDto> getPopularMoviesAsList(int page) {
        Movies movies = getPopularMovies(page);
        return movies != null && movies.getResults() != null ? movies.getResults() : List.of();
    }

    public List<MovieResponseDto> getTopRatedMoviesAsList(int page) {
        Movies movies = getTopRatedMovies(page);
        return movies != null && movies.getResults() != null ? movies.getResults() : List.of();
    }

    public List<MovieResponseDto> getUpcomingMoviesAsList(int page) {
        Movies movies = getUpcomingMovies(page);
        return movies != null && movies.getResults() != null ? movies.getResults() : List.of();
    }
}
