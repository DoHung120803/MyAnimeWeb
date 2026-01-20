package com.myanime.infrastructure.rest_client.notifies.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SendEmailRequest {
    private String subject;
    private String htmlContent;
    private Sender sender;
    private List<Recipient> to;

    @Setter
    @Getter
    public static class Sender {
        private String name;
        private String email;
    }

    @Setter
    @Getter
    public static class Recipient {
        private String name;
        private String email;
    }
}
