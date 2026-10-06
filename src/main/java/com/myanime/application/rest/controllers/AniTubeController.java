package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.anitube.AniTubeEmbedRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.ResponseFactory;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.port.input.AniTubeUC;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ani-tube")
public class AniTubeController {
    private final AniTubeUC aniTubeUC;

    @PostMapping("/embedded")
    public ResponseEntity<ApiResponse<Object>> embedded(
            @RequestBody @Valid AniTubeEmbedRequest request,
            @RequestParam(required = false, defaultValue = "home") String context
    ) throws BadRequestException {
        request.setContext(context);
        return ResponseFactory.success(aniTubeUC.getEmbeddedVideos(request));
    }
}
