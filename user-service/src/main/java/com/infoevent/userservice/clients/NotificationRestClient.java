package com.infoevent.userservice.clients;

import com.infoevent.userservice.entities.NotificationRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {
    Logger LOGGER = LoggerFactory.getLogger(NotificationRestClient.class);

    @PostMapping("/notification/register")
    @CircuitBreaker(name = "notificationservice", fallbackMethod = "defaultSendNotification")
    void sendNotification(@PathVariable NotificationRequest notificationRequest);

    default void defaultSendNotification(NotificationRequest notificationRequest, Throwable throwable) {
        LOGGER.error("Failed to send notification: {}", throwable.getMessage(), throwable);
    }
}
