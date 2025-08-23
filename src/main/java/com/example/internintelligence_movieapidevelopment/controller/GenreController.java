package com.example.internintelligence_movieapidevelopment.controller;

import com.example.internintelligence_movieapidevelopment.dto.request.GenreRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.GenreResponseDto;
import com.example.internintelligence_movieapidevelopment.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/get-all")
    public List<GenreResponseDto> getAll() {
        return genreService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public GenreResponseDto addGenre(@Valid @RequestBody GenreRequestDto requestDto) {
        return genreService.addGenre(requestDto);
    }

    @PutMapping("/edit/{id}")
    public GenreResponseDto editGenreByID(
            @PathVariable Long id,
            @Valid @RequestBody GenreRequestDto requestDto
    ) {
        return genreService.editGenre(id, requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void deleteByID(@PathVariable Long id) {
        genreService.deleteByID(id);
    }
}
