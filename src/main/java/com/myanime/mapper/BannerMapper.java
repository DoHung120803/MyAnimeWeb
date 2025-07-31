package com.myanime.mapper;

import com.myanime.entity.jpa.Banner;
import com.myanime.model.dto.response.BannerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerResponse toBannerResponse(Banner banner);
}
