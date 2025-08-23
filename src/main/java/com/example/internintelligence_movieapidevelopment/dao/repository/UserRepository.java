package com.example.internintelligence_movieapidevelopment.dao.repository;

import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findById(Integer id);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}
