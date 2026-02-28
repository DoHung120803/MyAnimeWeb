package com.myanime.domain.service.notifies.builder;

import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.enums.NotificationEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationEventFactory {

    private final List<NotificationEventBuilder> builders;

    public NotificationDTO build(NotificationEventType type, String receiver, Map<String, Object> meta)
            throws BadRequestException {

        return builders.stream()
                .filter(b -> b.supports() == type)
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        "Không tìm thấy builder cho loại thông báo: " + type.getCode()))
                .build(receiver, meta);
    }
}

