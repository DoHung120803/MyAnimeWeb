package com.myanime.infrastructure.jparepos.jpa;

import com.myanime.infrastructure.entities.jpa.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, String> {

    List<Anime> findByNameContaining(String name); // where name like '%name%'

//    List<Anime> findByNameIn(List<String> list); // where name like '%name%'
//    Page<Anime> findByNameContaining(String name, Pageable pageable); // where name like '%name%'

    List<Anime> findTop10ByOrderByViewsDesc(); // top 10 animes have the highest views

    List<Anime> findTop10ByOrderByRateDesc(); // top 10 animes have the highest rate

    boolean existsByName(String name); // check the exist anime

}
