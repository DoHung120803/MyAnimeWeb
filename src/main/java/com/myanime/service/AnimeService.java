package com.myanime.service;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.AnimeCreationRequest;
import com.myanime.model.dto.request.AnimeUpdateRequest;
import com.myanime.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService implements AnimeServiceInterface {
    @Autowired
    private AnimeRepository animeRepository;

    @Override
    public Anime createAnime(AnimeCreationRequest request) {
        Anime anime = new Anime();

        anime.setName(request.getName());
        anime.setRate(9.0);
        anime.setDescription(request.getDescription());
        anime.setViews(1000);
        anime.setIframe(request.getIframe());
        anime.setThumbnailUrl(request.getThumbnailUrl());

        return animeRepository.save(anime);
    }

    @Override
    public List<Anime> getAnimes() {
        return animeRepository.findAll();
    }

    @Override
    public Anime getAnime(String id) {
        return animeRepository.findById(id).orElseThrow(() -> new RuntimeException("Anime not found!!"));
    }

    @Override
    public Anime updateAnime(String id, AnimeUpdateRequest request) {
        Anime anime = getAnime(id);
        anime.setName(request.getName());
        anime.setDescription(request.getDescription());
        anime.setIframe(request.getIframe());
        anime.setThumbnailUrl(request.getThumbnailUrl());

        return animeRepository.save(anime);
    }

    @Override
    public void deleteAnime(String id) {
        animeRepository.deleteById(id);
    }
}
