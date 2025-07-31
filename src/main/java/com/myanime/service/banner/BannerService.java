package com.myanime.service.banner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.model.dto.request.banner.BannerCreationRequest;
import com.myanime.model.dto.response.BannerResponse;

import java.io.IOException;
import java.util.List;

public interface BannerService {
    BannerResponse createBanner(BannerCreationRequest request) throws IOException;
    List<BannerResponse> getAllBanners() throws JsonProcessingException;
}
