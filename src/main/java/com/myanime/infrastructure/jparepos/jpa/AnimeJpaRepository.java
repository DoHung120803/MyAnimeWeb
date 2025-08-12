package com.myanime.infrastructure.jparepos.jpa;

import com.myanime.infrastructure.entities.jpa.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimeJpaRepository extends JpaRepository<Anime, String> {

    Page<Anime> findByNameContaining(String name, Pageable pageable); // where name like '%name%'

//    List<Anime> findByNameIn(List<String> list); // where name like '%name%'
//    Page<Anime> findByNameContaining(String name, Pageable pageable); // where name like '%name%'

    List<Anime> findTop10ByOrderByViewsDesc(); // top 10 animes have the highest views

    List<Anime> findTop10ByOrderByRateDesc(); // top 10 animes have the highest rate

    boolean existsByName(String name); // check the exist anime

    @Query("SELECT a FROM Anime a WHERE :minId IS NULL OR a.id > :minId ORDER BY a.id ASC LIMIT :limit")
    List<Anime> findByMinIdAndLimit(@Param("minId") String minId, @Param("limit") Integer limit);

}
