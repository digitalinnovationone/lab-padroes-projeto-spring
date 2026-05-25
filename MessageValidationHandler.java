package com.yas.notificationapi.handler;

import com.yas.notificationapi.model.Notification;

public class MessageValidationHandler extends NotificationValidationHandler {

    @Override
    protected void check(Notification notification) {

        if (notification.getMessage().length() < 5) {
            throw new RuntimeException("Message too short");
        }
    }
}