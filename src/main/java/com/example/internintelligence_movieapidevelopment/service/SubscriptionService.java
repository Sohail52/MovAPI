package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.dao.entity.Subscription;
import com.example.internintelligence_movieapidevelopment.dao.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final JavaMailSender mailSender;

    public Subscription subscribe(String email, String genreName, Integer personTmdbId) {
        Subscription s = new Subscription();
        s.setEmail(email);
        s.setGenreName(genreName);
        s.setPersonTmdbId(personTmdbId);
        return subscriptionRepository.save(s);
    }

    public void unsubscribe(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public List<Subscription> list(String email) {
        return email == null ? subscriptionRepository.findAll() : subscriptionRepository.findByEmail(email);
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}


