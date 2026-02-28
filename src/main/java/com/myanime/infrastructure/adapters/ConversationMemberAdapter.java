package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.models.chats.ConversationMemberModel;
import com.myanime.domain.port.output.ConversationMemberRepository;
import com.myanime.infrastructure.entities.ConversationMember;
import com.myanime.infrastructure.jparepos.ConversationMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConversationMemberAdapter implements ConversationMemberRepository {

    private final ConversationMemberJpaRepository conversationMemberJpaRepository;

    @Override
    public void saveAll(List<ConversationMemberModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return;
        }

         conversationMemberJpaRepository.saveAll(ModelMapperUtil.mapList(
                models,
                ConversationMember.class
        ));
    }

    @Override
    public List<ConversationMemberModel> findByConversationIds(List<Long> conversationIds) {
        if (CollectionUtils.isEmpty(conversationIds)) {
            return List.of();
        }

        List<ConversationMember> entities = conversationMemberJpaRepository.findByConversationIdIn(conversationIds);

        return ModelMapperUtil.mapList(
                entities,
                ConversationMemberModel.class
        );
    }

    @Override
    public void incrementUnreadCountForOtherMembers(Long conversationId, String senderUserId) {
        conversationMemberJpaRepository.incrementUnreadCountForOtherMembers(conversationId, senderUserId);
    }

    @Override
    public void markAsRead(Long conversationId, String userId, Long lastReadMessageId) {
        conversationMemberJpaRepository.markAsRead(conversationId, userId, lastReadMessageId);
    }

    @Override
    public ConversationMemberModel findByConversationIdAndUserId(Long conversationId, String userId) {
        return conversationMemberJpaRepository.findByConversationIdAndUserId(conversationId, userId)
                .map(entity -> ModelMapperUtil.mapper(entity, ConversationMemberModel.class))
                .orElse(null);
    }

    @Override
    public int getTotalUnreadCount(String userId) {
        return conversationMemberJpaRepository.getTotalUnreadCount(userId);
    }
}
