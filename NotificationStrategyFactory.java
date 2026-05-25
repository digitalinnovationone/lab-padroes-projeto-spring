package com.yas.notificationapi.factory;

import com.yas.notificationapi.model.NotificationType;
import com.yas.notificationapi.strategy.NotificationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationStrategyFactory {

    private final Map<NotificationType, NotificationStrategy> strategies;

    public NotificationStrategyFactory(List<NotificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        NotificationStrategy::getType,
                        Function.identity()
                ));
    }

    public NotificationStrategy getStrategy(NotificationType type) {
        return strategies.get(type);
    }
}