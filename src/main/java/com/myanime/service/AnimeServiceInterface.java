package com.myanime.service;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.AnimeCreationRequest;
import com.myanime.model.dto.request.AnimeUpdateRequest;
import com.myanime.model.dto.response.AnimeResponse;

import java.util.List;

public interface AnimeServiceInterface {
    Anime createAnime(AnimeCreationRequest request);
    List<Anime> getAnimes();
    AnimeResponse getAnime(String id);
    AnimeResponse updateAnime(String id, AnimeUpdateRequest request);
    void deleteAnime(String id);
    List<Anime> findAnimeByName(String name);
    List<Anime> getTopViewsAnimes();
    List<Anime> getTopRateAnimes();

}
