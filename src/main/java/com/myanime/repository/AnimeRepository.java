package com.myanime.repository;

import com.myanime.entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, String> {
    List<Anime> findByNameContaining(String name); // where name like '%name%'
}
