package com.infoevent.gatewayservice.Services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j // Enable logging for this class
public class RouterValidatorImpl implements RouterValidator {

    private static final List<String> openEndpoints = List.of(
            "/auth/*",
            "/events", "/events/by-venue/*", "/events/{\\d+}",
            "/offertypes", "/offertypes/{\\d+}",
            "/prices/{\\d+}", "/prices/by-event/{\\d+}",
            "/notifications/register",
            "/users/", "/users/by-email",
            "/venues/", "/venues/{\\d+}",
            "/locations/", "/locations/{\\d+}",
            "/generate-key/*"
            );

    @Override
    public Predicate<ServerHttpRequest> isSecured() {
        return request -> {
            String requestPath = request.getURI().getPath();
            boolean isSecured = openEndpoints.stream()
                    .noneMatch(uri -> requestPath.matches(uri.replace("*", ".*")
                            .replace("{\\d+}", "\\d+")));
            log.debug("Request to {} is secured: {}", requestPath, isSecured);
            return isSecured;
        };
    }
}
