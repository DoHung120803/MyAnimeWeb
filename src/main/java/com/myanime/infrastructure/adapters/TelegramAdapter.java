package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.dtos.notifies.TelegramDTO;
import com.myanime.domain.port.output.TelegramRepository;
import com.myanime.infrastructure.rest_client.notifies.telegram.SendTelegramRequest;
import com.myanime.infrastructure.rest_client.notifies.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramAdapter implements TelegramRepository {

    private final TelegramService telegramService;

    @Override
    public void sendMessage(TelegramDTO telegram) {
        if (telegram == null) {
            return;
        }

        telegramService.sendMessage(ModelMapperUtil.mapper(telegram, SendTelegramRequest.class));
    }
}
