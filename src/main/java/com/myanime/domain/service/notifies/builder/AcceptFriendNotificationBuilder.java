package com.myanime.domain.service.notifies.builder;

import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.enums.NotificationEventType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AcceptFriendNotificationBuilder implements NotificationEventBuilder {

    @Override
    public NotificationEventType supports() {
        return NotificationEventType.ACCEPT_FRIEND;
    }

    @Override
    public NotificationDTO build(String receiver, Map<String, Object> meta) {
        NotificationDTO dto = new NotificationDTO();
        dto.setReceiver(receiver);
        dto.setTitle("Chấp nhận lời mời kết bạn");
        dto.setMessage("đã chấp nhận lời mời kết bạn của bạn");
        dto.setMetaData(Map.of(
                "type", NotificationEventType.ACCEPT_FRIEND.getCode(),
                "referenceId", meta.getOrDefault("referenceId", ""),
                "senderId", meta.getOrDefault("senderId", "")
        ));
        return dto;
    }
}

