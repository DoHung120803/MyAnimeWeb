package com.myanime.application.rest.controllers;

import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.domain.models.NotificationModel;
import com.myanime.domain.port.input.NotificationUC;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationUC notificationUC;

    @GetMapping
    public ApiResponse<Page<NotificationModel>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationModel> notifications = notificationUC.getNotifications(pageable);
        return ApiResponse.<Page<NotificationModel>>builder()
                .data(notifications)
                .message("Lấy danh sách thông báo thành công")
                .build();
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount() {
        long count = notificationUC.countUnread();
        return ApiResponse.<Long>builder()
                .data(count)
                .message("Lấy số thông báo chưa đọc thành công")
                .build();
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable Long id) {
        notificationUC.markAsRead(id);
        return ApiResponse.<Void>builder()
                .message("Đánh dấu đã đọc thành công")
                .build();
    }

    @PutMapping("/read-all")
    public ApiResponse<Void> markAllAsRead() {
        notificationUC.markAllAsRead();
        return ApiResponse.<Void>builder()
                .message("Đánh dấu tất cả đã đọc thành công")
                .build();
    }
}
