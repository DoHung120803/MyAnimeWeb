package com.myanime.domain.service.auth;

import com.myanime.application.rest.requests.authen.AuthenticationRequest;
import com.myanime.application.rest.requests.authen.IntrospectRequest;
import com.myanime.application.rest.responses.AuthenticationResponse;
import com.myanime.application.rest.responses.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationServiceInterface {
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
