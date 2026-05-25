package com.yas.notificationapi.controller;

import com.yas.notificationapi.dto.NotificationRequestDTO;
import com.yas.notificationapi.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<String> send(
            @RequestBody @Valid NotificationRequestDTO dto
    ) {

        notificationService.send(dto);

        return ResponseEntity.ok("Notification sent successfully");
    }
}