package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.NotificationModel;
import com.myanime.domain.port.output.NotificationRepository;
import com.myanime.infrastructure.entities.Notification;
import com.myanime.infrastructure.jparepos.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public NotificationModel save(NotificationModel notificationModel) {
        if (notificationModel == null) {
            return null;
        }

        Notification notification = ModelMapperUtil.mapper(notificationModel, Notification.class);
        Notification savedNotification = notificationJpaRepository.save(notification);
        
        return ModelMapperUtil.mapper(savedNotification, NotificationModel.class);
    }

    @Override
    public Optional<NotificationModel> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        Optional<Notification> notificationOpt = notificationJpaRepository.findById(id);
        return notificationOpt.map(entity -> ModelMapperUtil.mapper(entity, NotificationModel.class));
    }

    @Override
    public Page<NotificationModel> findByUserId(String userId, Pageable pageable) {
        if (!StringUtils.hasText(userId)) {
            return Page.empty();
        }

        Page<Notification> notificationPage = notificationJpaRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        return ModelMapperUtil.mapPage(notificationPage, NotificationModel.class);
    }

    @Override
    public List<NotificationModel> findUnreadByUserId(String userId, Pageable pageable) {
        if (!StringUtils.hasText(userId)) {
            return List.of();
        }

        List<Notification> notifications = notificationJpaRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(
                userId, false, pageable
        );
        
        return ModelMapperUtil.mapList(notifications, NotificationModel.class);
    }

    @Override
    public long countUnreadByUserId(String userId) {
        if (!StringUtils.hasText(userId)) {
            return 0;
        }

        return notificationJpaRepository.countByUserIdAndIsRead(userId, false);
    }

    @Override
    @Transactional
    public int markAllAsRead(String userId) {
        if (!StringUtils.hasText(userId)) {
            return 0;
        }

        return notificationJpaRepository.markAllAsReadByUserId(userId);
    }

    @Override
    @Transactional
    public int markAsRead(Long id, String userId) {
        if (id == null || !StringUtils.hasText(userId)) {
            return 0;
        }

        return notificationJpaRepository.markAsReadById(id, userId);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }

        notificationJpaRepository.deleteById(id);
    }
}

