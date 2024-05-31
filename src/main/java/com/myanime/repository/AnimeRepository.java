package com.myanime.repository;

import com.myanime.entity.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, String> {

    List<Anime> findByNameContaining(String name); // where name like '%name%'

//    List<Anime> findByNameIn(List<String> list); // where name like '%name%'
//    Page<Anime> findByNameContaining(String name, Pageable pageable); // where name like '%name%'

    List<Anime> findTop10ByOrderByViewsDesc(); // top 10 animes have highest views

    List<Anime> findTop10ByOrderByRateDesc(); // top 10 animes have highest rate

    boolean existsByName(String name); // check the exist anime

}
