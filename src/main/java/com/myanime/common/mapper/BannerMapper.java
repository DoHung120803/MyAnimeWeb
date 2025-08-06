package com.myanime.common.mapper;

import com.myanime.entity.jpa.Banner;
import com.myanime.application.rest.responses.BannerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerResponse toBannerResponse(Banner banner);
}
