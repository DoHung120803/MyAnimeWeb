package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.dtos.anitube.AniTubeEmbeddedVideoDTO;
import com.myanime.domain.port.output.TiktokRepository;
import com.myanime.infrastructure.rest_client.tiktok.TiktokService;
import com.myanime.infrastructure.rest_client.tiktok.responses.TikTokRecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TiktokAdapter implements TiktokRepository {

    private final TiktokService tiktokService;

    @Override
    public List<AniTubeEmbeddedVideoDTO> getRecommendVideos() {
        TikTokRecommendResponse tikTokRecommendResponse = tiktokService.recommendVideos();

        if (tikTokRecommendResponse == null || CollectionUtils.isEmpty(tikTokRecommendResponse.getItemList())) {
            return List.of();
        }

        return ModelMapperUtil.mapList(tikTokRecommendResponse.getItemList(), AniTubeEmbeddedVideoDTO.class);
    }
}
