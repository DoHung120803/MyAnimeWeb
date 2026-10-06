package com.myanime.domain.service.anitube.embed;

import com.myanime.application.rest.requests.anitube.AniTubeEmbedRequest;
import com.myanime.domain.enums.Provider;
import com.myanime.domain.port.output.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class YoutubeShortEmbedStrategy implements ProviderEmbedInterface {

    private final YoutubeRepository youtubeRepository;
    private final ExecutorService executorService;

    private static final short batchSize = 5;

    @Override
    public boolean supports(String provider) {
        return Provider.YOUTUBE.getType().equalsIgnoreCase(provider);
    }

    @Override
    public void validate(AniTubeEmbedRequest request) {
        // Implement validation logic for YouTube Shorts if needed
    }

    @Override
    public ProviderEmbedInterface execute() {
        return this;
    }

    @Override
    public Object getData(AniTubeEmbedRequest request) {
        return handleBatch(request, () -> executorService.submit(youtubeRepository::getShortVideos), batchSize);
    }
}
