package com.myanime.domain.service.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.entity.jpa.Anime;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.common.mapper.AnimeMapper;
import com.myanime.application.rest.requests.anime.AnimeCreationRequest;
import com.myanime.application.rest.requests.anime.AnimeUpdateRequest;
import com.myanime.application.rest.responses.AnimeResponse;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.repository.jpa.AnimeRepository;
import com.myanime.domain.service.redis.anime.AnimeRedisService;
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
    public List<AnimeResponse> getTopAnimes(String type) throws JsonProcessingException {
        List<AnimeResponse> topAnimes = animeRedisService.getTopAnime(type);

        if (topAnimes == null) {
            List<Anime> animes = switch (type) {
                case "views" -> animeRepository.findTop10ByOrderByViewsDesc();
                case "rate" -> animeRepository.findTop10ByOrderByRateDesc();
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };

            topAnimes = animes.stream()
                    .map(animeMapper::toAnimeResponse)
                    .toList();
            animeRedisService.saveTopAnime(topAnimes, type);
        }
        return topAnimes;
    }
}
