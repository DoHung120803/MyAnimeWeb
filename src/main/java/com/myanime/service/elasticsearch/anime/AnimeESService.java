package com.myanime.service.elasticsearch.anime;

import com.myanime.model.dto.response.AnimeResponse;

import java.util.List;

public interface AnimeESService {
    List<AnimeResponse> searchByNameOrDesc(String term);
}
