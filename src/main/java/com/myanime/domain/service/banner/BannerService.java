package com.myanime.domain.service.banner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.port.input.BannerUC;
import com.myanime.infrastructure.cache.CacheComponent;
import com.myanime.infrastructure.entities.Banner;
import com.myanime.common.mapper.BannerMapper;
import com.myanime.application.rest.requests.banner.BannerCreationRequest;
import com.myanime.application.rest.responses.BannerResponse;
import com.myanime.infrastructure.jparepos.BannerRepository;
import com.myanime.domain.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BannerService implements BannerUC {
    private final CloudinaryService cloudinaryService;
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final CacheComponent<String, String> cacheComponent;
    private final ObjectMapper objectMapper;

    private static final String CACHE_PREFIX = "MYANIME_BANNER_CACHE:get_all";

    @Override
    @Transactional
    public BannerResponse createBanner(BannerCreationRequest request) throws IOException, BadRequestException {
        String id = UUID.randomUUID().toString();
        String imageUrl = cloudinaryService.uploadFile(id, request.getImage());

        Banner banner = Banner.builder()
                .id(id)
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .build();

        bannerRepository.save(banner);
        return bannerMapper.toBannerResponse(banner);
    }

    @Override
    public List<BannerResponse> getAllBanners() throws JsonProcessingException {
        String data = cacheComponent.get(CACHE_PREFIX);
        if (data != null) {
            return objectMapper.readValue(data, new TypeReference<>() {
            });
        }

        List<BannerResponse> bannerResponses = ModelMapperUtil.mapList(
                bannerRepository.findAll(),
                BannerResponse.class
        );

        String json = objectMapper.writeValueAsString(bannerResponses);
        cacheComponent.set(CACHE_PREFIX, json, 1L, TimeUnit.HOURS);

        return bannerResponses;
    }
}
