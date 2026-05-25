package com.yas.notificationapi.service;

import com.yas.notificationapi.dto.NotificationRequestDTO;
import com.yas.notificationapi.factory.NotificationStrategyFactory;
import com.yas.notificationapi.handler.MessageValidationHandler;
import com.yas.notificationapi.handler.NotificationValidationHandler;
import com.yas.notificationapi.handler.RecipientValidationHandler;
import com.yas.notificationapi.model.Notification;
import com.yas.notificationapi.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationStrategyFactory strategyFactory;

    public void send(NotificationRequestDTO dto) {

        Notification notification = Notification.builder()
                .type(dto.getType())
                .recipient(dto.getRecipient())
                .message(dto.getMessage())
                .build();

        NotificationValidationHandler messageValidation =
                new MessageValidationHandler();

        NotificationValidationHandler recipientValidation =
                new RecipientValidationHandler();

        messageValidation.setNext(recipientValidation);

        messageValidation.validate(notification);

        NotificationStrategy strategy =
                strategyFactory.getStrategy(notification.getType());

        strategy.send(notification);
    }
}