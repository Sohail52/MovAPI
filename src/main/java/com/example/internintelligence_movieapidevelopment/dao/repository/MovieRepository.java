package com.example.internintelligence_movieapidevelopment.dao.repository;

import com.example.internintelligence_movieapidevelopment.dao.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
    Optional<Movie> findById(Integer id);

    Page<Movie> findAll(Specification<Movie> specification, Pageable pageable);

    boolean existsByTmdbId(Integer tmdbId);

    Optional<Movie> findByTmdbId(Integer tmdbId);
}

