package com.myanime.domain.service.auth;

import com.myanime.application.rest.requests.authen.AuthenticationRequest;
import com.myanime.application.rest.requests.authen.IntrospectRequest;
import com.myanime.application.rest.requests.authen.RefreshTokenRequest;
import com.myanime.application.rest.responses.AuthenticationResponse;
import com.myanime.application.rest.responses.IntrospectResponse;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.exceptions.LoginException;

public interface AuthenticationServiceInterface {
    IntrospectResponse introspect(IntrospectRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request) throws LoginException;
    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws BadRequestException;
}
