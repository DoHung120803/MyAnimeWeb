package com.myanime.domain.service.notifies.observers;

import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.dtos.notifies.TelegramDTO;
import com.myanime.domain.enums.NotifyType;
import com.myanime.domain.service.notifies.PostNotifyObserver;
import com.myanime.domain.port.output.TelegramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class TelegramNotifyObserver implements PostNotifyObserver {
    private final TelegramRepository telegramRepository;

    @Value("${telegram.bot.chat-id}")
    private String chatId;

    @Override
    public Short getSupportedType() {
        return NotifyType.TELEGRAM.getValue();
    }

    @Override
    public void sendNotification(NotificationDTO event) {
        if (event == null) {
            log.warn("Notification event is null, skip sending Telegram message");
            return;
        }

        if (chatId == null || chatId.isBlank()) {
            log.warn("Receiver (chatId) is null or blank, skip sending Telegram message");
            return;
        }

        String message = "Chào mừng " + event.getMetaData().getOrDefault("fullName", "") + " đến với MyAnime!";

        TelegramDTO telegramDTO = new TelegramDTO();
        telegramDTO.setChatId(chatId);
        telegramDTO.setText(message);

        telegramRepository.sendMessage(telegramDTO);

    }
}


