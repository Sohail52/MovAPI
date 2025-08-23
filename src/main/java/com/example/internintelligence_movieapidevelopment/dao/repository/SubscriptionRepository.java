package com.example.internintelligence_movieapidevelopment.dao.repository;

import com.example.internintelligence_movieapidevelopment.dao.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByEmail(String email);
}


