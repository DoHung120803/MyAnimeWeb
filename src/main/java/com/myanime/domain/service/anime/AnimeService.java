package com.myanime.domain.service.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.domain.port.input.AnimeUC;
import com.myanime.domain.port.output.AnimeRepository;
import com.myanime.infrastructure.entities.jpa.Anime;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.common.mapper.AnimeMapper;
import com.myanime.application.rest.requests.anime.AnimeCreationRequest;
import com.myanime.application.rest.requests.anime.AnimeUpdateRequest;
import com.myanime.application.rest.responses.AnimeResponse;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.infrastructure.jparepos.jpa.AnimeJpaRepository;
import com.myanime.domain.service.redis.anime.AnimeRedisService;
import com.myanime.infrastructure.models.AnimeModel;
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
public class AnimeService implements AnimeUC {
    AnimeJpaRepository animeJpaRepository;
    AnimeMapper animeMapper;
    ExecutorService executorService;
    AnimeRedisService animeRedisService;
    AnimeRepository animeRepository;

    @Override
    public AnimeResponse createAnime(AnimeCreationRequest request) {
        if (animeJpaRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ANIME_EXISTED);
        }

        Anime anime = animeMapper.toAnime(request);

        return animeMapper.toAnimeResponse(animeJpaRepository.save(anime));
    }

    @Override
    @Transactional
    public void createAnime(List<AnimeCreationRequest> request) {
        int chunkSize = 50;
        int totalSize = request.size();
        for (int i = 0; i < totalSize; i += chunkSize) {
            List<AnimeCreationRequest> chunk = request.subList(i, Math.min(i + chunkSize, totalSize));
            executorService.execute(() -> animeJpaRepository.saveAll(
                    chunk.stream().map(animeMapper::toAnime).toList())
            );
        }
    }

    @Override
    public PageResponse<AnimeResponse> getAnimes(int page, int size) throws JsonProcessingException {
        PageResponse<AnimeResponse> pageResponse = animeRedisService.getAllAnime(page - 1, size);

        if (pageResponse == null) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Anime> response = animeJpaRepository.findAll(pageable);

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
        return animeMapper.toAnimeResponse(animeJpaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANIME_NOT_FOUND)));
    }

    @Override
    public AnimeResponse updateAnime(String id, AnimeUpdateRequest request) {
        Anime anime = animeJpaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANIME_NOT_FOUND));
        animeMapper.updateAnime(anime, request);

        return animeMapper.toAnimeResponse(animeJpaRepository.save(anime));
    }

    @Override
    public void deleteAnime(String id) {
        animeJpaRepository.deleteById(id);
    }

    @Override
    public PageResponse<AnimeModel> search(String name, Pageable pageable) {
        Page<AnimeModel> page = animeRepository.search(name, pageable);

        return PageResponse.<AnimeModel>builder()
                .content(page.getContent())
                .currentPage(page.getNumber() + 1) // Page number is zero-based
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public List<AnimeResponse> getTopAnimes(String type) throws JsonProcessingException {
        List<AnimeResponse> topAnimes = animeRedisService.getTopAnime(type);

        if (topAnimes == null) {
            List<Anime> animes = switch (type) {
                case "views" -> animeJpaRepository.findTop10ByOrderByViewsDesc();
                case "rate" -> animeJpaRepository.findTop10ByOrderByRateDesc();
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
