package com.yas.notificationapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {

    private NotificationType type;

    private String recipient;

    private String message;
}