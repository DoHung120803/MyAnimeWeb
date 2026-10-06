package com.myanime.application.rest.requests.anitube;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniTubeEmbedRequest {

    @NotBlank(message = "provider không được để trống")
    private String provider;

    private String context;
}
