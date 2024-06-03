package com.myanime.service;

import com.myanime.entity.Anime;
import com.myanime.exception.AppException;
import com.myanime.exception.ErrorCode;
import com.myanime.mapper.AnimeMapper;
import com.myanime.model.dto.request.AnimeCreationRequest;
import com.myanime.model.dto.request.AnimeUpdateRequest;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.repository.AnimeRepository;
import com.myanime.utils.RandomUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AnimeService implements AnimeServiceInterface {
    AnimeRepository animeRepository;
    AnimeMapper animeMapper;

    @Override
    public Anime createAnime(AnimeCreationRequest request) {
        if (animeRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ANIME_EXISTED);
        }

        Anime anime = animeMapper.toAnime(request);
        anime.setRate(RandomUtils.randomRate());
        anime.setViews(RandomUtils.randomViews());

        return animeRepository.save(anime);
    }

    @Override
    public List<Anime> getAnimes() {
        return animeRepository.findAll();
    }

    @Override
    public AnimeResponse getAnime(String id) {
        return animeMapper.toAnimeResponse(animeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anime not found!!")));
    }

    @Override
    public AnimeResponse updateAnime(String id, AnimeUpdateRequest request) {
        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anime not found!!"));
        animeMapper.updateAnime(anime, request);

        return animeMapper.toAnimeResponse(animeRepository.save(anime));
    }

    @Override
    public void deleteAnime(String id) {
        animeRepository.deleteById(id);
    }

    public List<Anime> findAnimeByName(String name) {
        return animeRepository.findByNameContaining(name);
    }

//    public Page<Anime> findAll() {
//        Pageable pageable = PageRequest.of(0,5);
//        return animeRepository.findAll(pageable);
//    }


    @Override
    public List<Anime> getTopViewsAnimes() {
        return animeRepository.findTop10ByOrderByViewsDesc();
    }

    @Override
    public List<Anime> getTopRateAnimes() {
        return animeRepository.findTop10ByOrderByRateDesc();
    }
}
