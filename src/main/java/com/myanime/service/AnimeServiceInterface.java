package com.myanime.service;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.AnimeCreationRequest;
import com.myanime.model.dto.request.AnimeUpdateRequest;

import java.util.List;

public interface AnimeServiceInterface {
    Anime createAnime(AnimeCreationRequest request);
    List<Anime> getAnimes();
    Anime getAnime(String id);
    Anime updateAnime(String id, AnimeUpdateRequest request);
    void deleteAnime(String id);
}
