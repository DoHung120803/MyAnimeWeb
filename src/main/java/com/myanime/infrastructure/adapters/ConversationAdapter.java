package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.chats.ConversationModel;
import com.myanime.domain.port.output.ConversationRepository;
import com.myanime.infrastructure.entities.Conversation;
import com.myanime.infrastructure.jparepos.ConversationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ConversationAdapter implements ConversationRepository {
    private final ConversationJpaRepository conversationJpaRepository;

    @Override
    public ConversationModel save(ConversationModel conversation) {
        if (conversation == null) {
            return null;
        }

        Conversation entity = conversationJpaRepository.save(ModelMapperUtil.mapper(conversation, Conversation.class));

        return ModelMapperUtil.mapper(entity, ConversationModel.class);
    }

    @Override
    public ConversationModel findByDirectConversationKey(String directConversationKey) {
        if (!StringUtils.hasText(directConversationKey)) {
            return null;
        }

        Conversation entity = conversationJpaRepository.findByDirectConversationKey(directConversationKey);

        return ModelMapperUtil.mapper(entity, ConversationModel.class);
    }

    @Override
    public boolean existByDirectConversationKey(String directConversationKey) {
        if (!StringUtils.hasText(directConversationKey)) {
            return false;
        }

        return conversationJpaRepository.existsByDirectConversationKey(directConversationKey);
    }

    @Override
    public Long getIdByDirectKey(String directConversationKey) {
        if (!StringUtils.hasText(directConversationKey)) {
            return null;
        }

        return conversationJpaRepository.getIdByDirectConversationKey(directConversationKey);
    }

    @Override
    public boolean existById(Long conversationId) {
        if (conversationId == null) {
            return false;
        }

        return conversationJpaRepository.existsById(conversationId);
    }

    @Override
    public Page<ConversationModel> findConversationsByUserId(String userId, Pageable pageable) {
        if (!StringUtils.hasText(userId)) {
            return Page.empty();
        }

        Page<Conversation> conversations = conversationJpaRepository.findConversationsByUserId(userId, pageable);

        return ModelMapperUtil.mapPage(conversations, ConversationModel.class);
    }

    @Override
    public ConversationModel findById(Long conversationId) {
        if (conversationId == null) {
            return null;
        }

        Conversation entity = conversationJpaRepository.findById(conversationId).orElse(null);

        if (entity == null) {
            return null;
        }

        return ModelMapperUtil.mapper(entity, ConversationModel.class);
    }
}
