package com.yas.notificationapi.handler;

import com.yas.notificationapi.model.Notification;

public class RecipientValidationHandler extends NotificationValidationHandler {

    @Override
    protected void check(Notification notification) {

        if (notification.getRecipient().isBlank()) {
            throw new RuntimeException("Recipient invalid");
        }
    }
}