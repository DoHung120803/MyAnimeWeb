package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.genre.GenreCreationRequest;
import com.myanime.application.rest.requests.genre.GenreUpdateRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.GenreResponse;
import com.myanime.domain.port.input.GenreUC;
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
@RequestMapping("api/v1/genres")
public class GenreController {

    GenreUC genreUC;

    // Tạo mới thể loại (chỉ Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<GenreResponse> createGenre(@RequestBody @Valid GenreCreationRequest request) {
        return ApiResponse.<GenreResponse>builder()
                .data(genreUC.createGenre(request))
                .message("Tạo thể loại thành công")
                .build();
    }

    // Lấy tất cả thể loại (public)
    @GetMapping("")
    public ApiResponse<List<GenreResponse>> getAllGenres() {
        ApiResponse<List<GenreResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(genreUC.getAllGenres());
        return apiResponse;
    }

    // Lấy thông tin 1 thể loại theo ID (public)
    @GetMapping("/{id}")
    public ApiResponse<GenreResponse> getGenre(@PathVariable("id") String id) {
        ApiResponse<GenreResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(genreUC.getGenre(id));
        return apiResponse;
    }

    // Cập nhật thể loại (chỉ Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<GenreResponse> updateGenre(
            @PathVariable("id") String id,
            @RequestBody @Valid GenreUpdateRequest request) {
        ApiResponse<GenreResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(genreUC.updateGenre(id, request));
        return apiResponse;
    }

    // Xóa thể loại (chỉ Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable("id") String id) {
        genreUC.deleteGenre(id);
    }
}

