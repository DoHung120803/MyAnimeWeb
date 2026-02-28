package com.myanime.domain.service.notifies;

import com.myanime.common.utils.AuthUtil;
import com.myanime.domain.models.NotificationModel;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.input.NotificationUC;
import com.myanime.domain.port.output.NotificationRepository;
import com.myanime.domain.port.output.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUC {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public Page<NotificationModel> getNotifications(Pageable pageable) {
        String userId = AuthUtil.getCurrentUserId();
        Page<NotificationModel> page = notificationRepository.findByUserId(userId, pageable);
        enrichSenderUsername(page.getContent());
        return page;
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

    /**
     * Enrich senderUsername cho danh sách notifications dựa vào senderId.
     * senderUsername dùng để frontend navigate tới /profile/:username
     */
    private void enrichSenderUsername(List<NotificationModel> notifications) {
        List<String> senderIds = notifications.stream()
                .map(NotificationModel::getSenderId)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();

        if (senderIds.isEmpty()) return;

        Map<String, String> idToUsername = userRepository.findAllByIds(senderIds).stream()
                .collect(Collectors.toMap(
                        UserModel::getId,
                        UserModel::getUsername,
                        (a, b) -> a
                ));

        notifications.forEach(n -> {
            if (n.getSenderId() != null) {
                n.setSenderUsername(idToUsername.get(n.getSenderId()));
            }
        });
    }
}
