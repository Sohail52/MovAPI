package com.example.internintelligence_movieapidevelopment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableWebSecurity
@EnableCaching
@EnableScheduling
public class InternIntelligenceMovieApiDevelopmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternIntelligenceMovieApiDevelopmentApplication.class, args);
    }

}
