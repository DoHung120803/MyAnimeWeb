package com.myanime.domain.exceptions;

public class OAuth2Exception extends Exception {
    public OAuth2Exception(String message) {
        super(message);
    }

    public OAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
