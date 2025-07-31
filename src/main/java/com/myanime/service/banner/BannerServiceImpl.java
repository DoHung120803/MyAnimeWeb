package com.myanime.service.banner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myanime.config.caches.CacheComponent;
import com.myanime.entity.jpa.Banner;
import com.myanime.mapper.BannerMapper;
import com.myanime.model.dto.request.banner.BannerCreationRequest;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.model.dto.response.BannerResponse;
import com.myanime.repository.jpa.BannerRepository;
import com.myanime.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final CloudinaryService cloudinaryService;
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final CacheComponent<String, String> cacheComponent;
    private final ObjectMapper objectMapper;

    private static final String CACHE_PREFIX = "MYANIME_BANNER_CACHE:get_all";

    @Override
    @Transactional
    public BannerResponse createBanner(BannerCreationRequest request) throws IOException {
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

        List<BannerResponse> bannerResponses = bannerRepository.findAll().stream()
                .map(bannerMapper::toBannerResponse)
                .toList();

        String json = objectMapper.writeValueAsString(bannerResponses);
        cacheComponent.set(CACHE_PREFIX, json, 1L, TimeUnit.HOURS);

        return bannerResponses;
    }
}
