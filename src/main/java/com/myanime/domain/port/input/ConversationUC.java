package com.myanime.domain.port.input;

import com.myanime.application.rest.requests.chat.CreateConservationRequest;
import com.myanime.application.rest.requests.chat.GetDirectConversationRequest;
import com.myanime.application.rest.requests.chat.GetMessageRequest;
import com.myanime.application.rest.requests.chat.MarkAsReadRequest;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.models.chats.ConversationModel;
import org.springframework.data.domain.Pageable;

public interface ConversationUC {
    void createDirectConservation(CreateConservationRequest request) throws BadRequestException;

    Object getMessages(GetMessageRequest request, Pageable pageable);

    Object getDirectConversationMessages(GetDirectConversationRequest request, Pageable pageable);

    PageResponse<ConversationModel> getUserConversations(Pageable pageable);

    void markAsRead(MarkAsReadRequest request);

    int getTotalUnreadCount();
}
