package com.myanime.domain.service.banner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.application.rest.requests.banner.BannerCreationRequest;
import com.myanime.application.rest.responses.BannerResponse;

import java.io.IOException;
import java.util.List;

public interface BannerService {
    BannerResponse createBanner(BannerCreationRequest request) throws IOException;
    List<BannerResponse> getAllBanners() throws JsonProcessingException;
}
