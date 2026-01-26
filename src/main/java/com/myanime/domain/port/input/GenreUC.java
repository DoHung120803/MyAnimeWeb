package com.myanime.domain.port.input;

import com.myanime.application.rest.requests.genre.GenreCreationRequest;
import com.myanime.application.rest.requests.genre.GenreUpdateRequest;
import com.myanime.application.rest.responses.GenreResponse;

import java.util.List;

public interface GenreUC {
    GenreResponse createGenre(GenreCreationRequest request);
    List<GenreResponse> getAllGenres();
    GenreResponse getGenre(String id);
    GenreResponse updateGenre(String id, GenreUpdateRequest request);
    void deleteGenre(String id);
}

