package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.authen.AuthenticationRequest;
import com.myanime.application.rest.requests.authen.IntrospectRequest;
import com.myanime.application.rest.requests.authen.RefreshTokenRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.AuthenticationResponse;
import com.myanime.application.rest.responses.IntrospectResponse;
import com.myanime.application.rest.responses.ResponseFactory;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.exceptions.LoginException;
import com.myanime.domain.service.auth.AuthenticationServiceInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    AuthenticationServiceInterface authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse result = authenticationService.authenticate(request);
            return ResponseFactory.success(result);
        } catch (LoginException e) {
            return ResponseFactory.error(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        }
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) throws BadRequestException {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.refreshToken(request))
                .build();
    }
}


