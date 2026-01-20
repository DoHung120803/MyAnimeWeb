package com.myanime.infrastructure.rest_client.notifies.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final TelegramClient telegramClient;

    public void sendMessage(SendTelegramRequest request) {
        telegramClient.sendMessage(botToken, request);
    }
}
