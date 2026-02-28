package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.chat.CreateConservationRequest;
import com.myanime.application.rest.requests.chat.GetDirectConversationRequest;
import com.myanime.application.rest.requests.chat.GetMessageRequest;
import com.myanime.application.rest.requests.chat.MarkAsReadRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.models.chats.ConversationModel;
import com.myanime.domain.port.input.ConversationUC;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationUC conversationUC;

    @PostMapping("/direct")
    public ApiResponse<Void> createDirectConservation(@RequestBody @Valid CreateConservationRequest request) throws BadRequestException {
        conversationUC.createDirectConservation(request);
        return ApiResponse.<Void>builder()
                .message("Tạo cuộc trò chuyện thành công")
                .build();
    }

    @PostMapping("/messages")
    public ApiResponse<Object> getMessages(@RequestBody @Valid GetMessageRequest request, Pageable pageable) {
        return ApiResponse.builder()
                .data(conversationUC.getMessages(request, pageable))
                .build();
    }

    @PostMapping("/get-direct-conversation")
    public ApiResponse<Object> getDirectConversation(@RequestBody @Valid GetDirectConversationRequest request, Pageable pageable) {
        return ApiResponse.builder()
                .data(conversationUC.getDirectConversationMessages(request, pageable))
                .build();
    }

    @GetMapping("/get-all")
    public ApiResponse<PageResponse<ConversationModel>> getUserConversations(Pageable pageable) {
        return ApiResponse.<PageResponse<ConversationModel>>builder()
                .data(conversationUC.getUserConversations(pageable))
                .build();
    }

    @PutMapping("/mark-as-read")
    public ApiResponse<Void> markAsRead(@RequestBody @Valid MarkAsReadRequest request) {
        conversationUC.markAsRead(request);
        return ApiResponse.<Void>builder()
                .message("Đã đánh dấu đã đọc")
                .build();
    }

    @GetMapping("/unread-count")
    public ApiResponse<Integer> getTotalUnreadCount() {
        return ApiResponse.<Integer>builder()
                .data(conversationUC.getTotalUnreadCount())
                .build();
    }

}
