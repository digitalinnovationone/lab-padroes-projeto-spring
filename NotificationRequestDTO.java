package com.yas.notificationapi.dto;

import com.yas.notificationapi.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequestDTO {

    @NotNull
    private NotificationType type;

    @NotBlank
    private String recipient;

    @NotBlank
    private String message;
}