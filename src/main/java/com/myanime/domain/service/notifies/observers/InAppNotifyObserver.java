package com.myanime.domain.service.notifies.observers;

import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.enums.NotifyType;
import com.myanime.domain.models.NotificationModel;
import com.myanime.domain.models.UserModel;
import com.myanime.domain.port.output.NotificationRepository;
import com.myanime.domain.port.output.UserRepository;
import com.myanime.domain.service.notifies.PostNotifyObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InAppNotifyObserver implements PostNotifyObserver {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Short getSupportedType() {
        return NotifyType.IN_APP.getValue();
    }

    @Override
    public void sendNotification(NotificationDTO event) {
        try {
            String senderId = resolveSenderId(event);
            String senderName = resolveSenderName(senderId);
            String senderUsername = resolveSenderUsername(senderId);

            // Build nội dung thông báo kèm tên người gửi
            String content = buildContent(senderName, event.getMessage());

            // 1. Tạo NotificationModel và lưu vào DB
            NotificationModel model = new NotificationModel();
            model.setUserId(event.getReceiver());
            model.setSenderId(senderId);
            model.setType(resolveType(event));
            model.setReferenceId(resolveReferenceId(event));
            model.setContent(content);
            model.setIsRead(false);

            NotificationModel saved = notificationRepository.save(model);

            // 2. Set senderUsername (không persist vào DB, chỉ dùng cho realtime push)
            saved.setSenderUsername(senderUsername);

            // 3. Push realtime qua WebSocket tới user cụ thể
            messagingTemplate.convertAndSend(
                    "/notification/user/" + event.getReceiver(),
                    ApiResponse.<NotificationModel>builder()
                            .message("Bạn có thông báo mới")
                            .data(saved)
                            .build()
            );

            log.info("In-app notification sent to user {}: {}", event.getReceiver(), content);
        } catch (Exception e) {
            log.error("Failed to send in-app notification to user {}: {}", event.getReceiver(), e.getMessage(), e);
        }
    }

    /**
     * Build nội dung thông báo: "{Tên người gửi} {message}"
     * Ví dụ: "Nguyễn Văn A đã gửi cho bạn lời mời kết bạn"
     */
    private String buildContent(String senderName, String message) {
        if (senderName != null && !senderName.isBlank()) {
            return senderName + " " + message;
        }
        return message;
    }

    /**
     * Lấy tên hiển thị của sender từ DB (firstName + lastName)
     */
    private String resolveSenderName(String senderId) {
        if (senderId == null || senderId.isBlank()) return null;

        return userRepository.findById(senderId)
                .map(user -> {
                    String first = user.getFirstName() != null ? user.getFirstName() : "";
                    String last = user.getLastName() != null ? user.getLastName() : "";
                    String fullName = (first + " " + last).trim();
                    return fullName.isEmpty() ? user.getUsername() : fullName;
                })
                .orElse(null);
    }

    /**
     * Lấy username của sender để frontend navigate đến profile
     */
    private String resolveSenderUsername(String senderId) {
        if (senderId == null || senderId.isBlank()) return null;

        return userRepository.findById(senderId)
                .map(UserModel::getUsername)
                .orElse(null);
    }

    private String resolveSenderId(NotificationDTO event) {
        if (event.getMetaData() != null && event.getMetaData().containsKey("senderId")) {
            return String.valueOf(event.getMetaData().get("senderId"));
        }
        return null;
    }

    private String resolveType(NotificationDTO event) {
        if (event.getMetaData() != null && event.getMetaData().containsKey("type")) {
            return String.valueOf(event.getMetaData().get("type"));
        }
        return "GENERAL";
    }

    private String resolveReferenceId(NotificationDTO event) {
        if (event.getMetaData() != null && event.getMetaData().containsKey("referenceId")) {
            return String.valueOf(event.getMetaData().get("referenceId"));
        }
        return null;
    }
}
