package com.myanime.domain.service.anitube;

import com.myanime.application.rest.requests.anitube.AniTubeEmbedRequest;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.port.input.AniTubeUC;
import com.myanime.domain.service.anitube.embed.ProviderEmbedFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AniTubeService implements AniTubeUC {

    private final ProviderEmbedFactory providerEmbedFactory;

    @Override
    public Object getEmbeddedVideos(AniTubeEmbedRequest request) throws BadRequestException {
        String provider = request.getProvider();
        return providerEmbedFactory.getProviderEmbed(provider).getData(request);
    }
}
