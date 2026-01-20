package com.myanime.domain.port.output;

import com.myanime.domain.dtos.notifies.TelegramDTO;

public interface TelegramRepository {
    void sendMessage(TelegramDTO telegram);
}
