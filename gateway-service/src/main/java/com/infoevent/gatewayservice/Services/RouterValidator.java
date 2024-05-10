package com.infoevent.gatewayservice.Services;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * This interface defines a strategy for route validation within a reactive web application, focusing on security aspects.
 * It provides mechanisms to assess whether specific {@link ServerHttpRequest}s require secured access, such as authentication or authorization.
 * Implementations can define custom logic to evaluate request paths, headers, and other attributes to determine security requirements.
 */
public interface RouterValidator {

    /**
     * Determines whether a specific {@link ServerHttpRequest} requires authentication.
     * This method can be used to identify requests that must be authenticated but not necessarily authorized
     * for certain roles. It can inspect the request's path, headers, or other criteria to make this determination.
     *
     * @param request The {@link ServerHttpRequest} to evaluate.
     * @return {@code true} if the request requires authentication, {@code false} otherwise.
     */
    boolean requiresAuthentication(ServerHttpRequest request);

    /**
     * Evaluates whether a given {@link ServerHttpRequest} requires administrator-level access.
     * This method is intended to identify requests that not only require authentication but also specific authorization,
     * ensuring that only users with admin privileges can access certain routes. The evaluation can be based on the request's
     * path, headers, session information, or other factors relevant to determining administrative access.
     *
     * @param request The {@link ServerHttpRequest} to assess.
     * @return {@code true} if the request necessitates admin-level authorization, {@code false} otherwise.
     */
    boolean requiresAdmin(ServerHttpRequest request);
}
