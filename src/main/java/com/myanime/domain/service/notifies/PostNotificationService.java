package com.myanime.domain.service.notifies;

import com.myanime.domain.dtos.notifies.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostNotificationService {

    private final List<PostNotifyObserver> observers;

    public void post(NotificationDTO event, List<Short> notificationTypes) {
        if (event == null) {
            return;
        }

        List<PostNotifyObserver> supported = observers.stream()
                .filter(observer -> notificationTypes.contains(observer.getSupportedType()))
                .toList();

        for (PostNotifyObserver observer : supported) {
            observer.sendNotification(event);
        }
    }
}
