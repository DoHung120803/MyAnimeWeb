package com.myanime.infrastructure.rest_client.notifies.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${email.api.key}")
    private String apiKey;

    private final EmailClient emailClient;

    public void send(SendEmailRequest request) {
        emailClient.send(apiKey, request);
    }
}
