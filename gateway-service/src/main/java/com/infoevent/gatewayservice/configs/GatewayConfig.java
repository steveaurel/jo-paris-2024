package com.infoevent.gatewayservice.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {
    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter)
                                .addRequestHeader("Authorization", "Bearer {token}"))
                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .route("event-service", r -> r.path("/events/**", "/offertypes/**", "/prices/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://event-service"))
                .route("key-generator-service", r -> r.path("/generate-key/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://key-generator-service"))
                .route("notification-service", r -> r.path("/notifications/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://notification-service"))
                .route("payment-service", r -> r.path("/payments/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://payment-service"))
                .route("ticket-service", r -> r.path("/tickets/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ticket-service"))
                .route("venue-service", r -> r.path("/venues/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://venue-service"))
                .build();
    }
}
