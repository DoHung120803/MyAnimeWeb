package com.myanime.domain.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MediaInfoDTO {
    private Object size;
    private String url;
    private Short type;
    private String fileName;
}
