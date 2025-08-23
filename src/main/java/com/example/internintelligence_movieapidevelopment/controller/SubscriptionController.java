package com.example.internintelligence_movieapidevelopment.controller;

import com.example.internintelligence_movieapidevelopment.dao.entity.Subscription;
import com.example.internintelligence_movieapidevelopment.service.SubscriptionService;
import com.example.internintelligence_movieapidevelopment.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final MovieService movieService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subscription subscribe(@RequestParam String email,
                                  @RequestParam(required = false) String genreName,
                                  @RequestParam(required = false) Integer personTmdbId) {
        return subscriptionService.subscribe(email, genreName, personTmdbId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@PathVariable Long id) {
        subscriptionService.unsubscribe(id);
    }

    @GetMapping
    public List<Subscription> list(@RequestParam(required = false) String email) {
        return subscriptionService.list(email);
    }

    @PostMapping("/test-send")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendTestDigest(@RequestParam String email) {
        var upcoming = movieService.getUpcomingMoviesAsList(1);
        StringBuilder body = new StringBuilder();
        body.append("Upcoming highlights\n\n");
        upcoming.stream().limit(10).forEach(m -> {
            body.append("- ").append(m.getTitle()).append(" (⭐ ")
                .append(m.getVoteAverage()).append(")\n");
        });
        subscriptionService.sendEmail(email, "Movie Digest (Test)", body.toString());
    }
}


