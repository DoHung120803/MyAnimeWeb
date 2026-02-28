package com.myanime.domain.port.input;

import com.myanime.domain.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationUC {

    Page<NotificationModel> getNotifications(Pageable pageable);

    long countUnread();

    void markAsRead(Long notificationId);

    void markAllAsRead();
}
