package com.myanime.infrastructure.jparepos;

import com.myanime.infrastructure.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreJpaRepository extends JpaRepository<Genre, String> {
    boolean existsByName(String name);
}

