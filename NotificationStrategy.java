package com.yas.notificationapi.strategy;

import com.yas.notificationapi.model.Notification;
import com.yas.notificationapi.model.NotificationType;

public interface NotificationStrategy {

    void send(Notification notification);

    NotificationType getType();
}