package com.myanime.domain.service.notifies;

import com.myanime.domain.dtos.notifies.NotificationDTO;

public interface PostNotifyObserver {
    Short getSupportedType();
    void sendNotification(NotificationDTO event);
}
