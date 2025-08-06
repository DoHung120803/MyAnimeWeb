package com.myanime.domain.service.elasticsearch.anime;

import com.myanime.application.rest.responses.AnimeResponse;

import java.util.List;

public interface AnimeESService {
    List<AnimeResponse> searchByNameOrDesc(String term);
}
