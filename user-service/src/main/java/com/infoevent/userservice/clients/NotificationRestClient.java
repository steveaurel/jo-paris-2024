package com.infoevent.userservice.clients;

import com.infoevent.userservice.entities.NotificationRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationRestClient {
    @PostMapping("/notification/register")
    @CircuitBreaker(name = "notificationservice", fallbackMethod = "sendDefaultMail")
    void sendNotification(@PathVariable NotificationRequest notificationRequest);
}
