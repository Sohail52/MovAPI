package com.example.internintelligence_movieapidevelopment.dao.repository;

import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import com.example.internintelligence_movieapidevelopment.dao.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository  extends JpaRepository<Watchlist,Long> {
    boolean existsByUserAndMovie(User user, Movie movie);

    Optional<Watchlist> findByUserAndMovie(User user,Movie movie);

    List<Watchlist> findAllByUser(User user);
}
