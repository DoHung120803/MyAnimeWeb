package com.myanime.domain.port.output;

import com.myanime.infrastructure.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre save(Genre genre);
    List<Genre> findAll();
    Optional<Genre> findById(String id);
    void deleteById(String id);
    boolean existsById(String id);
    boolean existsByName(String name);
}

