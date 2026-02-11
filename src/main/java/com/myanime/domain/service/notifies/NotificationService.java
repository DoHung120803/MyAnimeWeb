package com.myanime.domain.service.notifies;

import com.myanime.common.utils.AuthUtil;
import com.myanime.domain.models.NotificationModel;
import com.myanime.domain.port.input.NotificationUC;
import com.myanime.domain.port.output.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUC {

    private final NotificationRepository notificationRepository;

    @Override
    public Page<NotificationModel> getNotifications(Pageable pageable) {
        String userId = AuthUtil.getCurrentUserId();
        return notificationRepository.findByUserId(userId, pageable);
    }

    @Override
    public long countUnread() {
        String userId = AuthUtil.getCurrentUserId();
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        String userId = AuthUtil.getCurrentUserId();
        notificationRepository.markAsRead(notificationId, userId);
    }

    @Override
    public void markAllAsRead() {
        String userId = AuthUtil.getCurrentUserId();
        notificationRepository.markAllAsRead(userId);
    }
}
