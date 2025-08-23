package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.client.TmdbClient;
import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import com.example.internintelligence_movieapidevelopment.dao.entity.Watchlist;
import com.example.internintelligence_movieapidevelopment.dao.repository.*;
import com.example.internintelligence_movieapidevelopment.dto.response.WatchlistResponseDto;
import com.example.internintelligence_movieapidevelopment.exception.AlreadyExistException;
import com.example.internintelligence_movieapidevelopment.exception.ResourceNotFoundException;
import com.example.internintelligence_movieapidevelopment.mapper.WatchlistMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistMapper watchlistMapper;
    private final MovieRepository movieRepository;
    private final TmdbClient tmdbClient;
    private final UserRepository userRepository;


    public WatchlistResponseDto addToWatchlist(Integer movieId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Attempting to add movie ID '{}' to user's - {} watchlist.", movieId, currentUser);
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            // For development: create mock movie if ID is in mock range (1-20)
            if (movieId >= 1 && movieId <= 20) {
                movie = createMockMovie(movieId);
                movieRepository.save(movie);
                log.info("Created mock movie for ID: {}", movieId);
            } else {
                // Try TMDB id; if present, fetch and persist minimal movie details
                Integer tmdbId = movieId;
                movie = movieRepository.findByTmdbId(tmdbId).orElse(null);
                if (movie == null) {
                    var dto = tmdbClient.fetchMovieDetails(tmdbId, "en-US");
                    if (dto == null) {
                        log.warn("Failed to fetch TMDB details for id {}", tmdbId);
                        throw new ResourceNotFoundException("MOVIE_NOT_FOUND");
                    }
                    movie = new Movie();
                    movie.setTmdbId(tmdbId);
                    movie.setTitle(dto.getTitle());
                    movie.setOverview(dto.getOverview());
                    movie.setReleaseDate(dto.getReleaseDate());
                    movie.setVoteAverage(dto.getVoteAverage());
                    // Ensure runtime is set to a sane default if TMDB doesn't provide it
                    Integer runtime = dto.getRuntime();
                    if (runtime == null || runtime < 30) {
                        runtime = 120; // default runtime to satisfy NOT NULL and min expectations
                    }
                    movie.setRuntime(runtime);
                    movieRepository.save(movie);
                }
            }
        }

        User user = userRepository.findByUsername(currentUser).orElseThrow(() -> {
            log.warn("Failed to add movie to watchlist: user '{}' not found", currentUser);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });

        if (watchlistRepository.existsByUserAndMovie(user, movie)) {
            log.warn("Movie already exists in the watchlist.");
            throw new AlreadyExistException("MOVIE_ALREADY_EXISTS_IN_WATCHLIST");
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setMovie(movie);
        watchlistRepository.save(watchlist);

        log.info("Successfully added movie ID '{}' to user '{}' watchlist", movieId, currentUser);
        return watchlistMapper.toDto(watchlist);
    }

    private Movie createMockMovie(Integer movieId) {
        Movie movie = new Movie();
        movie.setTmdbId(movieId + 1000); // Use a different range for mock TMDB IDs
        
        // Set mock data based on ID
        switch (movieId) {
            case 1:
                movie.setTitle("Inception");
                movie.setOverview("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.");
                movie.setReleaseDate(LocalDate.of(2010, 7, 16));
                movie.setVoteAverage(8.4);
                break;
            case 2:
                movie.setTitle("The Shawshank Redemption");
                movie.setOverview("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
                movie.setReleaseDate(LocalDate.of(1994, 9, 23));
                movie.setVoteAverage(8.7);
                break;
            case 3:
                movie.setTitle("The Dark Knight");
                movie.setOverview("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
                movie.setReleaseDate(LocalDate.of(2008, 7, 18));
                movie.setVoteAverage(8.5);
                break;
            case 4:
                movie.setTitle("Pulp Fiction");
                movie.setOverview("The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.");
                movie.setReleaseDate(LocalDate.of(1994, 10, 14));
                movie.setVoteAverage(8.5);
                break;
            case 5:
                movie.setTitle("The Matrix");
                movie.setOverview("A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.");
                movie.setReleaseDate(LocalDate.of(1999, 3, 31));
                movie.setVoteAverage(8.1);
                break;
            case 6:
                movie.setTitle("Interstellar");
                movie.setOverview("A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.");
                movie.setReleaseDate(LocalDate.of(2014, 11, 7));
                movie.setVoteAverage(8.3);
                break;
            default:
                movie.setTitle("Mock Movie " + movieId);
                movie.setOverview("A mock movie for development purposes.");
                movie.setReleaseDate(LocalDate.now());
                movie.setVoteAverage(7.0);
        }
        
        movie.setRuntime(120); // Default runtime
        return movie;
    }


    public List<WatchlistResponseDto> getWatchlist() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Attempting to get user's watchlist - '{}' ", currentUser);
        User user = userRepository.findByUsername(currentUser).orElseThrow(() -> {
            log.warn("Failed to get watchlist: user '{}' not found", currentUser);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        List<Watchlist> watchlist = watchlistRepository.findAllByUser(user);
        return watchlist
                .stream()
                .map(watchlistMapper::toDto)
                .toList();
    }


    public void deleteFromWatchlist(Integer movieId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Attempting to delete movie ID '{}' from user's watchlist-{}", movieId, currentUser);

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> {
            log.error("Failed to delete movie from watchlist: movie ID '{}' not found", movieId);
            return new ResourceNotFoundException("MOVIE_NOT_FOUND");
        });

        User user = userRepository.findByUsername(currentUser).orElseThrow(() -> {
            log.error("Failed to delete movie from watchlist: user '{}' not found", currentUser);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });

        Watchlist watchlist = watchlistRepository.findByUserAndMovie(user, movie).orElseThrow(() -> {
            log.error("Failed to find movie ID '{}' in user '{}' watchlist", movieId, currentUser);
            return new ResourceNotFoundException("WATCHLIST_ENTRY_NOT_FOUND");
        });

        watchlistRepository.delete(watchlist);
        log.info("Successfully deleted movie ID '{}' from user '{}' watchlist", movieId, currentUser);
    }


}
