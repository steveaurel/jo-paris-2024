package com.infoevent.ticketservice.client;

import com.infoevent.ticketservice.entities.NotificationRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {
    @PostMapping("/notifications/confirmation")
    @CircuitBreaker(name = "notificationservice", fallbackMethod = "sendDefaultMail")
    void sendNotification(@PathVariable NotificationRequest notificationRequest);
}
