package com.myanime.domain.port.input;

import com.myanime.application.rest.requests.chat.SendMessageRequest;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.models.chats.MessageModel;

import java.security.Principal;

public interface ChatUC {
    MessageModel sendMessage(SendMessageRequest request, Principal principal) throws BadRequestException;
}
