package com.myanime.domain.port.input;

import com.myanime.application.rest.requests.anitube.AniTubeEmbedRequest;
import com.myanime.common.exceptions.BadRequestException;

public interface AniTubeUC {
    Object getEmbeddedVideos(AniTubeEmbedRequest request) throws BadRequestException;
}
