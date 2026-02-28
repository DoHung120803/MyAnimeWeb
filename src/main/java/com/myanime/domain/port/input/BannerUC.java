package com.myanime.domain.port.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.application.rest.requests.banner.BannerCreationRequest;
import com.myanime.application.rest.responses.BannerResponse;
import com.myanime.common.exceptions.BadRequestException;

import java.io.IOException;
import java.util.List;

public interface BannerUC {
    BannerResponse createBanner(BannerCreationRequest request) throws IOException, BadRequestException;
    List<BannerResponse> getAllBanners() throws JsonProcessingException;
}
