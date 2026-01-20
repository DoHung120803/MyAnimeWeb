package com.myanime.common.exceptions;

import com.myanime.infrastructure.configurations.feign.ExceptionMessage;

public class FeignClientException extends Exception {
    private final ExceptionMessage exceptionMessage;

    public FeignClientException(String message, ExceptionMessage exceptionMessage) {
        super(message);
        this.exceptionMessage = exceptionMessage;
    }

    public ExceptionMessage getExceptionMessage() {
        return exceptionMessage;
    }
}
