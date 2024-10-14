package com.myanime.service.auth;

import com.myanime.model.dto.request.authen.AuthenticationRequest;
import com.myanime.model.dto.request.authen.IntrospectRequest;
import com.myanime.model.dto.response.AuthenticationResponse;
import com.myanime.model.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationServiceInterface {
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
