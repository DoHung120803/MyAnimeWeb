package com.myanime.infrastructure.rest_client.notifies.telegram;

import com.myanime.infrastructure.configurations.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "telegram-bot-api",
        url = "https://api.telegram.org",
        configuration = FeignConfig.class
)
public interface TelegramClient {
    @PostMapping(value = "/bot{botToken}/sendMessage", produces = MediaType.APPLICATION_JSON_VALUE)
    void sendMessage(@PathVariable("botToken") String botToken, @RequestBody SendTelegramRequest body);
}
