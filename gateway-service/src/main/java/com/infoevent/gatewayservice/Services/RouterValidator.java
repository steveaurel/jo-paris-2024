package com.infoevent.gatewayservice.Services;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.function.Predicate;

/**
 * Interface defining a strategy for validating routes in a reactive web application context.
 * It supplies a method to determine if a given {@link ServerHttpRequest} requires secured access.
 */
public interface RouterValidator {

    /**
     * Provides a predicate that tests whether a {@link ServerHttpRequest} should be secured.
     * This method can be implemented to specify conditions under which routes require authentication
     * and/or authorization. For example, it might check the path, headers, or other properties of the request
     * to determine if it matches secured routes.
     *
     * @return A {@link Predicate} that returns true if the request should be subject to security checks,
     * and false otherwise.
     */
    Predicate<ServerHttpRequest> isSecured();
}
