package com.myanime.domain.service.genre;

import com.myanime.application.rest.requests.genre.GenreCreationRequest;
import com.myanime.application.rest.requests.genre.GenreUpdateRequest;
import com.myanime.application.rest.responses.GenreResponse;
import com.myanime.common.exceptions.AppException;
import com.myanime.common.exceptions.ErrorCode;
import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.port.input.GenreUC;
import com.myanime.domain.port.output.GenreRepository;
import com.myanime.infrastructure.entities.Genre;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreService implements GenreUC {

    GenreRepository genreRepository;

    @Override
    public GenreResponse createGenre(GenreCreationRequest request) {
        if (genreRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.GENRE_EXISTED);
        }

        Genre genre = ModelMapperUtil.mapper(request, Genre.class);
        Genre savedGenre = genreRepository.save(genre);

        return ModelMapperUtil.mapper(savedGenre, GenreResponse.class);
    }

    @Override
    public List<GenreResponse> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return ModelMapperUtil.mapList(genres, GenreResponse.class);
    }

    @Override
    public GenreResponse getGenre(String id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND));

        return ModelMapperUtil.mapper(genre, GenreResponse.class);
    }

    @Override
    public GenreResponse updateGenre(String id, GenreUpdateRequest request) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.GENRE_NOT_FOUND));

        ModelMapperUtil.mapper(request, genre);
        Genre updatedGenre = genreRepository.save(genre);

        return ModelMapperUtil.mapper(updatedGenre, GenreResponse.class);
    }

    @Override
    public void deleteGenre(String id) {
        if (!genreRepository.existsById(id)) {
            throw new AppException(ErrorCode.GENRE_NOT_FOUND);
        }
        genreRepository.deleteById(id);
    }
}

