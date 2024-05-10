package com.infoevent.ticketservice.client;

import com.infoevent.ticketservice.entities.NotificationRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {
    Logger LOGGER = LoggerFactory.getLogger(NotificationRestClient.class);

    @PostMapping("/notifications/confirmation")
    @CircuitBreaker(name = "notificationservice", fallbackMethod = "defaultSendNotification")
    void sendNotification(@PathVariable NotificationRequest notificationRequest);

    default void defaultSendNotification(NotificationRequest notificationRequest, Throwable throwable) {
        LOGGER.error("Failed to send notification: {}", throwable.getMessage(), throwable);
    }
}
