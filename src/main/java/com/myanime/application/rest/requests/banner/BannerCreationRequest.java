package com.myanime.application.rest.requests.banner;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BannerCreationRequest {
    private MultipartFile image;
    private String description;
}
