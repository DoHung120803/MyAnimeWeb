package com.myanime.domain.service.notifies;

import com.myanime.domain.dtos.notifies.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.myanime.common.constants.GlobalConstant.SUPPORTED_NOTIFY_TYPES;

@Service
@RequiredArgsConstructor
public class PostNotificationService {

    private final List<PostNotifyObserver> observers;

    public void post(NotificationDTO event) {
        if (event == null) {
            return;
        }

        List<PostNotifyObserver> supported = observers.stream()
                .filter(observer -> SUPPORTED_NOTIFY_TYPES.contains(observer.getSupportedType()))
                .toList();

        for (PostNotifyObserver observer : supported) {
            observer.sendNotification(event);
        }
    }
}
