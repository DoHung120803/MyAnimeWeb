package com.myanime.common.mapper;

import com.myanime.infrastructure.entities.jpa.Anime;
import com.myanime.application.rest.requests.anime.AnimeCreationRequest;
import com.myanime.application.rest.requests.anime.AnimeUpdateRequest;
import com.myanime.application.rest.responses.AnimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnimeMapper {
    @Mapping(target = "views", expression = "java(com.myanime.common.utils.RandomUtils.randomViews())")
    @Mapping(target = "rate", expression = "java(com.myanime.common.utils.RandomUtils.randomRate())")
    Anime toAnime(AnimeCreationRequest request);
    void updateAnime(@MappingTarget Anime anime, AnimeUpdateRequest request);
    AnimeResponse toAnimeResponse(Anime anime);
}
