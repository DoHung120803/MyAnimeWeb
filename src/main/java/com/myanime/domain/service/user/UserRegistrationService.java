package com.myanime.domain.service.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.myanime.common.utils.JsonUtil;
import com.myanime.domain.dtos.UserDTO;
import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.inputs.consumers.ConsumeInput;
import com.myanime.domain.port.input.consumers.ConsumerUC;
import com.myanime.domain.service.notifies.PostNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserRegistrationService implements ConsumerUC {

    private final PostNotificationService postNotificationService;

    @Override
    public void consume(ConsumeInput input) {
        String value = input.getRecordItem().value();

        UserDTO userInfo = JsonUtil.jsonToObject(value, new TypeReference<>() {});

        if (userInfo == null) {
            return;
        }

        NotificationDTO event = buildNotificationEvent(userInfo);
        postNotificationService.post(event);
    }

    private NotificationDTO buildNotificationEvent(UserDTO userInfo) {
        NotificationDTO event = new NotificationDTO();

        String fullName = userInfo.getFirstName() + " " + userInfo.getLastName();

        event.setReceiver(userInfo.getEmail());
        event.setTitle("Welcome to MyAnime, " + fullName + "!");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("fullName", fullName);
        event.setMetaData(metadata);

        return event;
    }
}
