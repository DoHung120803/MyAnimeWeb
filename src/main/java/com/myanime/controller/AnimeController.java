package com.myanime.controller;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.AnimeCreationRequest;
import com.myanime.model.dto.request.AnimeUpdateRequest;
import com.myanime.model.dto.request.ApiResponse;
import com.myanime.service.AnimeServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnimeController {
    @Autowired
    private AnimeServiceInterface animeService;

    // add anime into db
    @PostMapping("/upload/store")
    public ApiResponse<Anime> createAnime(@ModelAttribute @Valid AnimeCreationRequest request) {
        ApiResponse<Anime> apiResponse = new ApiResponse<>();

        apiResponse.setData(animeService.createAnime(request));

        return apiResponse;
    }

    // get all anime
    @GetMapping("/animes")
    public ApiResponse<List<Anime>> getAnimes() {
        ApiResponse<List<Anime>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.getAnimes());
        return apiResponse;
    }

    @GetMapping("/animes/{id}")
    public ApiResponse<Anime> getAnime(@PathVariable("id") String id) {
        ApiResponse<Anime> apiResponse = new ApiResponse<>();
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
    public ApiResponse<Anime> updateAnime(@PathVariable("id") String id,@ModelAttribute AnimeUpdateRequest request) {
        ApiResponse<Anime> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeService.updateAnime(id, request));
        return apiResponse;
    }

    @DeleteMapping("/animes/delete/{id}")
    public void deleteAnime(@PathVariable String id) {
        animeService.deleteAnime(id);
    }
}
