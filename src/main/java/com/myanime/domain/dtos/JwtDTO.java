package com.myanime.domain.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JwtDTO {
    private String jwt;
    private LocalDateTime expireAt;
    private Long expireTime;
}
