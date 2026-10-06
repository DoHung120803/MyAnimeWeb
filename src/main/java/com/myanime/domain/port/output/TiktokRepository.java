package com.myanime.domain.port.output;

import com.myanime.domain.dtos.anitube.AniTubeEmbeddedVideoDTO;

import java.util.List;

public interface TiktokRepository {
    List<AniTubeEmbeddedVideoDTO> getRecommendVideos();
}
