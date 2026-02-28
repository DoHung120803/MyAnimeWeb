package com.myanime.domain.service.notifies.builder;

import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.enums.NotificationEventType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FriendRequestNotificationBuilder implements NotificationEventBuilder {

    @Override
    public NotificationEventType supports() {
        return NotificationEventType.FRIEND_REQUEST;
    }

    @Override
    public NotificationDTO build(String receiver, Map<String, Object> meta) {
        NotificationDTO dto = new NotificationDTO();
        dto.setReceiver(receiver);
        dto.setTitle("Lời mời kết bạn");
        dto.setMessage("đã gửi cho bạn lời mời kết bạn");
        dto.setMetaData(Map.of(
                "type", NotificationEventType.FRIEND_REQUEST.getCode(),
                "referenceId", meta.getOrDefault("referenceId", ""),
                "senderId", meta.getOrDefault("senderId", "")
        ));
        return dto;
    }
}

