package com.myanime.domain.dtos.notifies;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailDTO {
    private String subject;
    private String htmlContent;
    private Sender sender;
    private List<Recipient> to;

    @Getter
    @Setter
    public static class Sender {
        private String name;
        private String email;
    }

    @Getter
    @Setter
    public static class Recipient {
        private String name;
        private String email;
    }
}
