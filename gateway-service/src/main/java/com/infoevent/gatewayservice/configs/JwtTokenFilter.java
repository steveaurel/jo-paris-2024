package com.infoevent.gatewayservice.configs;

import com.infoevent.gatewayservice.Services.JwtUtils;
import com.infoevent.gatewayservice.Services.RouterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter implements GlobalFilter, Ordered {
    private final RouterValidator validator;
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        boolean requiresAuth = validator.requiresAuthentication(exchange.getRequest());
        boolean requiresAdmin = validator.requiresAdmin(exchange.getRequest());

        if(requiresAuth || requiresAdmin){
            if (authMissing(exchange.getRequest())) {
                log.info("Authorization header is missing in request.");
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            final String token = extractToken(exchange.getRequest());
            if (!jwtUtils.validateToken(token)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            else if (jwtUtils.isExpired(token)) {
                log.info("Authorization token is expired.");
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            else if (requiresAdmin && !hasAdminRole(token)) {
                log.info("Operation requires ADMIN role.");
                return onError(exchange, HttpStatus.FORBIDDEN);
            }
        }

        // Forward the request to the next filter in the chain if authentication succeeds or isn't required
        return chain.filter(exchange);
    }
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    /**
     * Checks if the Authorization header is missing in the request.
     *
     * @param request The current server HTTP request.
     * @return true if the Authorization header is missing, false otherwise.
     */
    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization") || request.getHeaders().getOrEmpty("Authorization").isEmpty();
    }

    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getOrEmpty("Authorization").get(0);
        return bearerToken.substring(7); // Assume token is prefixed with "Bearer "
    }

    /**
     * Determines if the request is to an endpoint requiring ADMIN role.
     *
     * @param request The server HTTP request.
     * @return True if the endpoint requires ADMIN role, false otherwise.
     */
    private boolean requiresAdminRole(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        boolean modificationRequest = HttpMethod.PUT.equals(method) || HttpMethod.DELETE.equals(method);

        boolean isEventAdminPath = path.matches("/events") && HttpMethod.POST.equals(method) ||
                path.matches("/events/\\d+") && modificationRequest ||
                path.matches("/events/\\d+/update-seats") && HttpMethod.PATCH.equals(method);

        boolean isOfferTypeAdminPath = path.matches("/offertypes") && HttpMethod.POST.equals(method) ||
                path.matches("/offertypes/\\d+") && modificationRequest;

        boolean isPriceAdminPath = (path.matches("/prices") && HttpMethod.POST.equals(method)) ||
                (path.matches("/prices/\\d+") && modificationRequest) ||
                (path.matches("/prices") && HttpMethod.GET.equals(method));

        boolean isPaymentAdminPath = path.matches("/payments") && HttpMethod.POST.equals(method) ||
                path.matches("/payments/\\d+") && HttpMethod.GET.equals(method) ||
                path.matches("/payments/by-user/\\d+") && HttpMethod.GET.equals(method);

        boolean isTicketAdminPath = path.matches("/ticket/by-event/\\d+") || (path.matches("/ticket") && request.getMethod() == HttpMethod.GET);

        boolean isUserAdminPath = path.matches("/users/?$") && HttpMethod.GET.equals(method) ||
                path.matches("/users/\\d+$") && HttpMethod.DELETE.equals(method);

        boolean isVenueAdminPath = path.matches("/venues") && (HttpMethod.POST.equals(method) || modificationRequest);

        boolean isLocationAdminPath = path.matches("/locations") && (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.DELETE.equals(method));

        return isEventAdminPath || isOfferTypeAdminPath || isPriceAdminPath || isPaymentAdminPath || isTicketAdminPath || isUserAdminPath || isVenueAdminPath || isLocationAdminPath;
    }

    /**
     * Checks if the JWT token contains the ADMIN role.
     *
     * @param token The JWT token.
     * @return True if the token contains

     */
    private boolean hasAdminRole(String token) {
        return jwtUtils.getClaims(token).get("role", String.class).contains("ADMIN");
    }


    @Override
    public int getOrder() {
        return -100;
    }
}
