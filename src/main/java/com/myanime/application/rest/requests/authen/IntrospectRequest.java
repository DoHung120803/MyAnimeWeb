package com.myanime.application.rest.requests.authen;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectRequest {
    @NotBlank(message = "Access token không được để trống")
    String token;
}
