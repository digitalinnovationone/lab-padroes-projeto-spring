package com.yas.notificationapi.strategy;

import com.yas.notificationapi.model.Notification;
import com.yas.notificationapi.model.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(Notification notification) {
        log.info("Sending EMAIL to {}", notification.getRecipient());
    }

    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL;
    }
}