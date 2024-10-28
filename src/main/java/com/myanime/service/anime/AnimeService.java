package com.myanime.service.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.entity.jpa.Anime;
import com.myanime.exception.AppException;
import com.myanime.exception.ErrorCode;
import com.myanime.mapper.AnimeMapper;
import com.myanime.model.dto.request.anime.AnimeCreationRequest;
import com.myanime.model.dto.request.anime.AnimeUpdateRequest;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.model.dto.response.PageResponse;
import com.myanime.repository.jpa.AnimeRepository;
import com.myanime.service.redis.anime.AnimeRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AnimeService implements AnimeServiceInterface {
    AnimeRepository animeRepository;
    AnimeMapper animeMapper;
    ExecutorService executorService;
    AnimeRedisService animeRedisService;

    @Override
    public AnimeResponse createAnime(AnimeCreationRequest request) {
        if (animeRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ANIME_EXISTED);
        }

        Anime anime = animeMapper.toAnime(request);

        return animeMapper.toAnimeResponse(animeRepository.save(anime));
    }

    @Override
    @Transactional
    public void createAnime(List<AnimeCreationRequest> request) {
        int chunkSize = 50;
        int totalSize = request.size();
        for (int i = 0; i < totalSize; i += chunkSize) {
            List<AnimeCreationRequest> chunk = request.subList(i, Math.min(i + chunkSize, totalSize));
            executorService.execute(() -> animeRepository.saveAll(
                    chunk.stream().map(animeMapper::toAnime).toList())
            );
        }
    }

    @Override
    public PageResponse<AnimeResponse> getAnimes(int page, int size) throws JsonProcessingException {
        PageResponse<AnimeResponse> pageResponse = animeRedisService.getAllAnime(page - 1, size);

        if (pageResponse == null) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Anime> response = animeRepository.findAll(pageable);

            pageResponse = PageResponse.<AnimeResponse>builder()
                    .content(response.stream()
                            .map(animeMapper::toAnimeResponse)
                            .toList())
                    .currentPage(page)
                    .pageSize(size)
                    .totalElements(response.getTotalElements())
                    .totalPages(response.getTotalPages())
                    .build();
            animeRedisService.saveAllAnime(pageResponse);
        }
        return pageResponse;
    }

    @Override
    public AnimeResponse getAnime(String id) {
        return animeMapper.toAnimeResponse(animeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANIME_NOT_FOUND)));
    }

    @Override
    public AnimeResponse updateAnime(String id, AnimeUpdateRequest request) {
        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANIME_NOT_FOUND));
        animeMapper.updateAnime(anime, request);

        return animeMapper.toAnimeResponse(animeRepository.save(anime));
    }

    @Override
    public void deleteAnime(String id) {
        animeRepository.deleteById(id);
    }

    @Override
    public List<AnimeResponse> findAnimeByName(String name) {
        return animeRepository.findByNameContaining(name)
                .stream()
                .map(animeMapper::toAnimeResponse)
                .toList();
    }

    @Override
    public List<Anime> getTopViewsAnimes() {
        return animeRepository.findTop10ByOrderByViewsDesc();
    }

    @Override
    public List<Anime> getTopRateAnimes() {
        return animeRepository.findTop10ByOrderByRateDesc();
    }
}
