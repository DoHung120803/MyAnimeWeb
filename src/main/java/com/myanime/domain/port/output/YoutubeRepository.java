package com.myanime.domain.port.output;

import com.myanime.domain.dtos.anitube.AniTubeEmbeddedVideoDTO;

import java.util.List;

public interface YoutubeRepository {
    List<AniTubeEmbeddedVideoDTO> getShortVideos();
}
