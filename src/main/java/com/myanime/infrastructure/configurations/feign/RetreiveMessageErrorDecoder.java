package com.myanime.infrastructure.configurations.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myanime.common.exceptions.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage message = null;

        if (response.body() != null) {
            try (InputStream bodyIs = response.body()
                    .asInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                message = mapper.readValue(bodyIs, ExceptionMessage.class);
                message.setStatus(response.status());

                if (message.getMessage() == null) {
                    message.setMessage(message.getError());
                }

                return new FeignClientException("FeignClient error", message);

            } catch (IOException e) {
                Sentry.setExtra("response", response.toString());
                Sentry.captureException(e);

                message = new ExceptionMessage();

                message.setStatus(response.status());
                message.setMessage(response.toString());

                return new FeignClientException("FeignClient error", message);
            }
        }

        message = new ExceptionMessage();

        message.setStatus(response.status());
        message.setMessage(response.reason());

        return switch (response.status()) {
            case 401 -> new FeignClientException("Unauthenticated", message);
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}