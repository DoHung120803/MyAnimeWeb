package com.myanime.application.rest.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerResponse {
    private String id;
    private String imageUrl;
    private String description;
    private String name;
}
