package com.myanime.infrastructure.adapters;

import com.myanime.domain.port.output.GenreRepository;
import com.myanime.infrastructure.entities.Genre;
import com.myanime.infrastructure.jparepos.GenreJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreAdapter implements GenreRepository {

    private final GenreJpaRepository genreJpaRepository;

    @Override
    public Genre save(Genre genre) {
        return genreJpaRepository.save(genre);
    }

    @Override
    public List<Genre> findAll() {
        return genreJpaRepository.findAll();
    }

    @Override
    public Optional<Genre> findById(String id) {
        return genreJpaRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        genreJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return genreJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return genreJpaRepository.existsByName(name);
    }
}

