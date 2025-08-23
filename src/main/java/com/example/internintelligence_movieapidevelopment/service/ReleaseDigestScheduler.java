package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.dao.entity.Subscription;
import com.example.internintelligence_movieapidevelopment.dao.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReleaseDigestScheduler {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final MovieService movieService;

    // Run weekly at 9AM Monday
    @Scheduled(cron = "0 0 9 * * MON")
    public void sendWeeklyDigest() {
        List<Subscription> subs = subscriptionRepository.findAll();
        if (subs.isEmpty()) return;

        // Simple: reuse upcoming movies page 1 and send to all subscribers
        var upcoming = movieService.getUpcomingMoviesAsList(1);
        StringBuilder body = new StringBuilder();
        body.append("Upcoming this week (as of ").append(LocalDate.now()).append(")\n\n");
        upcoming.stream().limit(10).forEach(m -> {
            body.append("- ").append(m.getTitle()).append(" (⭐ ")
                .append(m.getVoteAverage()).append(")\n");
        });
        String text = body.toString();

        for (Subscription s : subs) {
            subscriptionService.sendEmail(s.getEmail(), "Weekly Movie Digest", text);
        }
    }
}


