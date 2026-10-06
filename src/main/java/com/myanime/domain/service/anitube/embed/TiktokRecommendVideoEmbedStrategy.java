package com.myanime.domain.service.anitube.embed;

import com.myanime.application.rest.requests.anitube.AniTubeEmbedRequest;
import com.myanime.domain.enums.Provider;
import com.myanime.domain.port.output.TiktokRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@RequiredArgsConstructor
public class TiktokRecommendVideoEmbedStrategy implements ProviderEmbedInterface {

    private final TiktokRepository tiktokRepository;
    private static final short batchSize = 5;
    private final ExecutorService executorService;

    @Override
    public boolean supports(String provider) {
        return Provider.TIKTOK.getType().equalsIgnoreCase(provider);
    }

    @Override
    public void validate(AniTubeEmbedRequest request) {
        // Implement validation logic for TikTok if needed
    }

    @Override
    public ProviderEmbedInterface execute() {
        return this;
    }

    @Override
    public Object getData(AniTubeEmbedRequest request) {
        String context = request.getContext();

        if ("home".equalsIgnoreCase(context)) return tiktokRepository.getRecommendVideos();

        return handleBatch(request, () -> executorService.submit(tiktokRepository::getRecommendVideos), batchSize);
    }
}
