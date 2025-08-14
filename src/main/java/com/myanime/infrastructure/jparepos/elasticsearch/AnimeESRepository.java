package com.myanime.infrastructure.jparepos.elasticsearch;

import com.myanime.infrastructure.entities.elasticsearch.AnimeES;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AnimeESRepository extends ElasticsearchRepository<AnimeES, String> {
    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["name^3", "description"],
                "fuzziness": "AUTO"
              }
            }
            """)
    List<AnimeES> searchByNameOrDesc(String term, Pageable pageable);
}
