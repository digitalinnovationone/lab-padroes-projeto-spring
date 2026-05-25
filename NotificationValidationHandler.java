package com.yas.notificationapi.handler;

import com.yas.notificationapi.model.Notification;

public abstract class NotificationValidationHandler {

    private NotificationValidationHandler next;

    public NotificationValidationHandler setNext(NotificationValidationHandler next) {
        this.next = next;
        return next;
    }

    public void validate(Notification notification) {

        check(notification);

        if (next != null) {
            next.validate(notification);
        }
    }

    protected abstract void check(Notification notification);
}