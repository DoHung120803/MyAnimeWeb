package com.myanime.service.anime;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.anime.AnimeCreationRequest;
import com.myanime.model.dto.request.anime.AnimeUpdateRequest;
import com.myanime.model.dto.response.AnimeResponse;

import java.util.List;

public interface AnimeServiceInterface {
    AnimeResponse createAnime(AnimeCreationRequest request);
    void createAnime(List<AnimeCreationRequest> request);
    List<Anime> getAnimes();
    AnimeResponse getAnime(String id);
    AnimeResponse updateAnime(String id, AnimeUpdateRequest request);
    void deleteAnime(String id);
    List<Anime> findAnimeByName(String name);
    List<Anime> getTopViewsAnimes();
    List<Anime> getTopRateAnimes();

}
