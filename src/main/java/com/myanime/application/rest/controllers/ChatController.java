package com.myanime.application.rest.controllers;

import com.myanime.application.rest.requests.chat.SendMessageRequest;
import com.myanime.application.rest.requests.chat.SendTypingRequest;
import com.myanime.application.rest.responses.ApiResponse;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.models.chats.MessageModel;
import com.myanime.domain.models.chats.TypingEventModel;
import com.myanime.domain.port.input.ChatUC;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatUC chatUC;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/send-message")
    @SendTo("/conversation")
    public ApiResponse<MessageModel> sendMessage(@RequestBody @Valid SendMessageRequest request, Principal principal) throws BadRequestException {
        MessageModel savedMessage = chatUC.sendMessage(request, principal);
        return ApiResponse.<MessageModel>builder()
                .message("Gửi tin nhắn thành công")
                .data(savedMessage)
                .build();
    }

    @MessageMapping("/typing")
    @SendTo("/conversation/typing")
    public ApiResponse<TypingEventModel> sendTypingStatus(@RequestBody @Valid SendTypingRequest request, Principal principal) {
        TypingEventModel typingEvent = new TypingEventModel();
        typingEvent.setConversationId(request.getConversationId());
        typingEvent.setUserId(principal.getName());
        typingEvent.setIsTyping(request.getIsTyping());

        return ApiResponse.<TypingEventModel>builder()
                .message("Typing status updated")
                .data(typingEvent)
                .build();
    }
}
