package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.dao.entity.Genre;
import com.example.internintelligence_movieapidevelopment.dao.repository.GenreRepository;
import com.example.internintelligence_movieapidevelopment.dto.request.GenreRequestDto;
import com.example.internintelligence_movieapidevelopment.dto.response.GenreResponseDto;
import com.example.internintelligence_movieapidevelopment.exception.AlreadyExistException;
import com.example.internintelligence_movieapidevelopment.exception.ResourceNotFoundException;
import com.example.internintelligence_movieapidevelopment.mapper.GenreMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;


    public List<GenreResponseDto> getAll() {
        log.info("Attempting to get all genres");
        List<Genre> genres = genreRepository.findAll();
        return genres.stream().map(genreMapper::toDto).toList();
    }

    public GenreResponseDto addGenre(GenreRequestDto requestDto) {
        log.info("Attempting add new genre: {}", requestDto.getName());

        if (genreRepository.existsByNameIgnoreCase(requestDto.getName())) {
            log.error("Failed to add genre. Genre '{}' already exists.", requestDto.getName());
            throw new AlreadyExistException("GENRE_ALREADY_EXISTS");
        }

        Genre genre = genreMapper.toEntity(requestDto);
        genreRepository.save(genre);

        log.info("Genre '{}' successfully added.", requestDto.getName());

        return genreMapper.toDto(genre);
    }

    public GenreResponseDto editGenre(Long id, GenreRequestDto requestDto) {
        log.info("Attempting edit genre with ID: {}", id);
        Genre genre = genreRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to update genre. Genre ID '{}' doesn't exist", id);
            return new ResourceNotFoundException("GENRE_NOT_FOUND");
        });
        genreMapper.mapForUpdate(genre, requestDto);
        genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    public void deleteByID(Long id) {
        log.info("Attempting to delete genre with ID: {}", id);

        if (!genreRepository.existsById(id)) {
            log.error("Failed to delete genre. Genre '{}' doesn't exists.", id);
            throw new ResourceNotFoundException("GENRE_NOT_FOUND");
        }

        genreRepository.deleteById(id);
        log.info("Genre with ID '{}' successfully deleted", id);
    }

}
