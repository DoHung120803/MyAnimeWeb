package com.myanime.infrastructure.rest_client.notifies.email;

import com.myanime.infrastructure.configurations.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "notify-service",
        url = "${feign.client.email-host}",
        configuration = FeignConfig.class
)
public interface EmailClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    void send(@RequestHeader("api-key") String apiKey, @RequestBody SendEmailRequest body);
}
