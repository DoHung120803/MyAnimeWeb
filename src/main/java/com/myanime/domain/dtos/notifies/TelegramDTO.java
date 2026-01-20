package com.myanime.domain.dtos.notifies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TelegramDTO {
    private String chatId;
    private String text;
}
