package com.yas.notificationapi.strategy;

import com.yas.notificationapi.model.Notification;
import com.yas.notificationapi.model.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(Notification notification) {
        log.info("Sending SMS to {}", notification.getRecipient());
    }

    @Override
    public NotificationType getType() {
        return NotificationType.SMS;
    }
}