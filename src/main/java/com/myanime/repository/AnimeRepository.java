package com.myanime.repository;

import com.myanime.entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<Anime, String> {
}
