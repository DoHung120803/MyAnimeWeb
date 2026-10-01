package com.myanime.application.rest.requests.authen;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2ExchangeRequest {
    @NotBlank(message = "Code không được để trống")
    private String code;
}
