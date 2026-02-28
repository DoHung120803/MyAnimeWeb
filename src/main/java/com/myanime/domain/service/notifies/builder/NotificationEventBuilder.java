package com.myanime.domain.service.notifies.builder;

import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.enums.NotificationEventType;

import java.util.Map;

public interface NotificationEventBuilder {
    NotificationEventType supports();
    NotificationDTO build(String receiver, Map<String, Object> meta);
}

