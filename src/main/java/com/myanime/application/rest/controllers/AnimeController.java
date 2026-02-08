package com.myanime.application.rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.application.rest.requests.anime.AnimeCreationRequest;
import com.myanime.application.rest.requests.anime.AnimeUpdateRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.AnimeResponse;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.domain.port.input.AnimeUC;
import com.myanime.domain.models.AnimeModel;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1/animes")
public class AnimeController {
    AnimeUC animeUC;

    // add anime into db
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<AnimeResponse> createAnime(
            @ModelAttribute @Valid AnimeCreationRequest request) {
        return ApiResponse.<AnimeResponse>builder()
                .data(animeUC.createAnime(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/chunk")
    public ApiResponse<Void> createAnime(@RequestBody List<AnimeCreationRequest> request) {
        if (request == null || request.isEmpty()) {
            throw new AppException(ErrorCode.LIST_EMPTY);
        }
        animeUC.createAnime(request);

        return ApiResponse.<Void>builder()
                .message("Create anime successfully")
                .build();
    }

    // get all anime
    @GetMapping("")
    public ApiResponse<PageResponse<AnimeResponse>> getAnimes(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "30") int size
    ) throws JsonProcessingException {
        ApiResponse<PageResponse<AnimeResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeUC.getAnimes(page, size));
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<AnimeResponse> getAnime(@PathVariable("id") String id) {
        ApiResponse<AnimeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeUC.getAnime(id));
        return apiResponse;
    }

    @GetMapping("/top-animes")
    public ApiResponse<List<AnimeResponse>> getHighestViewsAnimes(
            @RequestParam(required = false, defaultValue = "views") String type
    ) throws JsonProcessingException {
        if (!type.equals("views") && !type.equals("rate")) {
            type = "views";
        }
        ApiResponse<List<AnimeResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeUC.getTopAnimes(type));
        return apiResponse;
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<AnimeModel>> search(@RequestParam String name, Pageable pageable) {
        ApiResponse<PageResponse<AnimeModel>> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeUC.search(name, pageable));

        return apiResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<AnimeResponse> updateAnime(@PathVariable("id") String id, @ModelAttribute AnimeUpdateRequest request) {
        ApiResponse<AnimeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(animeUC.updateAnime(id, request));
        return apiResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteAnime(@PathVariable String id) {
        animeUC.deleteAnime(id);
    }
}
