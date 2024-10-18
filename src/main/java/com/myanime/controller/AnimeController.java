package com.myanime.controller;

import com.myanime.entity.Anime;
import com.myanime.exception.AppException;
import com.myanime.exception.ErrorCode;
import com.myanime.model.dto.request.anime.AnimeCreationRequest;
import com.myanime.model.dto.request.anime.AnimeUpdateRequest;
import com.myanime.model.dto.response.ApiResponse;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.service.anime.AnimeServiceInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1/animes")
public class AnimeController {
    AnimeServiceInterface animeService;

    // add anime into db
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<AnimeResponse> createAnime(
            @ModelAttribute @Valid AnimeCreationRequest request) {
        return ApiResponse.<AnimeResponse>builder()
                .data(animeService.createAnime(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/chunk")
    public void createAnime(@RequestBody List<AnimeCreationRequest> request) {
        if (request == null || request.isEmpty()) {
            throw new AppException(ErrorCode.LIST_EMPTY);
        }
        animeService.createAnime(request);
    }

    // get all anime
    @GetMapping("")
    public ApiResponse<List<Anime>> getAnimes() {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getAnimes());
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<AnimeResponse> getAnime(@PathVariable("id") String id) {
        ApiResponse<AnimeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getAnime(id));
        return apiResponse;
    }

    @GetMapping("/top-views")
    public ApiResponse<List<Anime>> getHighestViewsAnimes() {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getTopViewsAnimes());
        return apiResponse;
    }

    @GetMapping("/top-rate")
    public ApiResponse<List<Anime>> getHighestRateAnimes() {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getTopRateAnimes());
        return apiResponse;
    }

    @GetMapping("/search")
    public ApiResponse<List<Anime>> findAnimesByName(@RequestParam String name) {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.findAnimeByName(name));
        return apiResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<AnimeResponse> updateAnime(@PathVariable("id") String id,@ModelAttribute AnimeUpdateRequest request) {
        ApiResponse<AnimeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.updateAnime(id, request));
        return apiResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAnime(@PathVariable String id) {
        animeService.deleteAnime(id);
    }
}
