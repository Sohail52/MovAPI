package com.example.internintelligence_movieapidevelopment.controller;

import com.example.internintelligence_movieapidevelopment.dto.response.WatchlistResponseDto;
import com.example.internintelligence_movieapidevelopment.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping("get-all")
    public List<WatchlistResponseDto> getWatchlist() {
        return watchlistService.getWatchlist();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{movieId}")
    public WatchlistResponseDto addToWatchlist(@PathVariable Integer movieId) {
        return watchlistService.addToWatchlist(movieId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{movieId}")
    public void deleteFromWatchlist(@PathVariable Integer movieId) {
        watchlistService.deleteFromWatchlist(movieId);
    }


}
