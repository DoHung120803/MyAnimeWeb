package com.myanime.common.mapper;

import com.myanime.entity.elasticsearch.AnimeES;
import com.myanime.entity.jpa.Anime;
import com.myanime.application.rest.requests.anime.AnimeCreationRequest;
import com.myanime.application.rest.requests.anime.AnimeUpdateRequest;
import com.myanime.application.rest.responses.AnimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimeMapper {
    @Mapping(target = "views", expression = "java(com.myanime.common.utils.RandomUtils.randomViews())")
    @Mapping(target = "rate", expression = "java(com.myanime.common.utils.RandomUtils.randomRate())")
    Anime toAnime(AnimeCreationRequest request);
    void updateAnime(@MappingTarget Anime anime, AnimeUpdateRequest request);
    AnimeResponse toAnimeResponse(Anime anime);
    List<AnimeResponse> toAnimeResponseList(List<AnimeES> list);
}
