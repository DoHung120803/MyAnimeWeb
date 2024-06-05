package com.myanime.controller;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.anime.AnimeCreationRequest;
import com.myanime.model.dto.request.anime.AnimeUpdateRequest;
import com.myanime.model.dto.response.ApiResponse;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.service.AnimeServiceInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class AnimeController {
    AnimeServiceInterface animeService;

    // add anime into db
    @PostMapping("/upload/store")
    public ApiResponse<AnimeResponse> createAnime(@ModelAttribute @Valid AnimeCreationRequest request) {
        return ApiResponse.<AnimeResponse>builder()
                .data(animeService.createAnime(request))
                .build();
    }

    // get all anime
    @GetMapping("/animes")
    public ApiResponse<List<Anime>> getAnimes() {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getAnimes());
        return apiResponse;
    }

    @GetMapping("/animes/{id}")
    public ApiResponse<AnimeResponse> getAnime(@PathVariable("id") String id) {
        ApiResponse<AnimeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getAnime(id));
        return apiResponse;
    }

    @GetMapping("/animes/top-views")
    public ApiResponse<List<Anime>> getHighestViewsAnimes() {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getTopViewsAnimes());
        return apiResponse;
    }

    @GetMapping("/animes/top-rate")
    public ApiResponse<List<Anime>> getHighestRateAnimes() {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getTopRateAnimes());
        return apiResponse;
    }

    @GetMapping("/animes/search")
    public ApiResponse<List<Anime>> findAnimesByName(@RequestParam String name) {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.findAnimeByName(name));
        return apiResponse;
    }

    @PutMapping("/animes/update/{id}")
    public ApiResponse<AnimeResponse> updateAnime(@PathVariable("id") String id,@ModelAttribute AnimeUpdateRequest request) {
        ApiResponse<AnimeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.updateAnime(id, request));
        return apiResponse;
    }

    @DeleteMapping("/animes/delete/{id}")
    public void deleteAnime(@PathVariable String id) {
        animeService.deleteAnime(id);
    }
}
