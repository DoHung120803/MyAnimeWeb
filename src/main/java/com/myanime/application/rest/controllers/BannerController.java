package com.myanime.application.rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.application.rest.requests.banner.BannerCreationRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.BannerResponse;
import com.myanime.domain.port.input.BannerUC;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerUC bannerService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BannerResponse> createBanner(BannerCreationRequest request) throws IOException {
        if (request.getImage() == null) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }
        BannerResponse bannerResponse = bannerService.createBanner(request);

        return ApiResponse.<BannerResponse>builder()
                .data(bannerResponse)
                .build();
    }

    @GetMapping("")
    public ApiResponse<List<BannerResponse>> getAllBanners() throws JsonProcessingException {
        return ApiResponse.<List<BannerResponse>>builder()
                .data(bannerService.getAllBanners())
                .build();
    }
}
