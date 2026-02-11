package com.myanime.domain.port.output;

import com.myanime.domain.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    NotificationModel save(NotificationModel notificationModel);
    
    Optional<NotificationModel> findById(Long id);
    
    Page<NotificationModel> findByUserId(String userId, Pageable pageable);
    
    List<NotificationModel> findUnreadByUserId(String userId, Pageable pageable);
    
    long countUnreadByUserId(String userId);
    
    int markAllAsRead(String userId);
    
    int markAsRead(Long id, String userId);
    
    void deleteById(Long id);
}

