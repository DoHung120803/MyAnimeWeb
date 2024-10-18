package com.myanime.mapper;

import com.myanime.entity.Anime;
import com.myanime.model.dto.request.anime.AnimeCreationRequest;
import com.myanime.model.dto.request.anime.AnimeUpdateRequest;
import com.myanime.model.dto.response.AnimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimeMapper {
    Anime toAnime(AnimeCreationRequest request);
    void updateAnime(@MappingTarget Anime anime, AnimeUpdateRequest request);
    AnimeResponse toAnimeResponse(Anime anime);

    @Mapping(target = "rate", source = "RandomUtils.randomRate()")
    @Mapping(target = "views", source = "RandomUtils.randomViews()")
    List<Anime> toAnimeList(List<AnimeCreationRequest> request);
}
