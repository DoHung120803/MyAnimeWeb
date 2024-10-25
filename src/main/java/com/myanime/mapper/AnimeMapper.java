package com.myanime.mapper;

import com.myanime.entity.elasticsearch.AnimeES;
import com.myanime.entity.jpa.Anime;
import com.myanime.model.dto.request.anime.AnimeCreationRequest;
import com.myanime.model.dto.request.anime.AnimeUpdateRequest;
import com.myanime.model.dto.response.AnimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimeMapper {
    @Mapping(target = "views", expression = "java(com.myanime.utils.RandomUtils.randomViews())")
    @Mapping(target = "rate", expression = "java(com.myanime.utils.RandomUtils.randomRate())")
    Anime toAnime(AnimeCreationRequest request);
    void updateAnime(@MappingTarget Anime anime, AnimeUpdateRequest request);
    AnimeResponse toAnimeResponse(Anime anime);
    List<AnimeResponse> toAnimeResponseList(List<AnimeES> list);
}
