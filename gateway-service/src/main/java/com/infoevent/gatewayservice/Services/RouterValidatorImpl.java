package com.infoevent.gatewayservice.Services;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RouterValidatorImpl implements RouterValidator {

    @Override
    public boolean requiresAuthentication(ServerHttpRequest request) {
        String path = request.getPath().toString();
        HttpMethod method = request.getMethod();

        if ((method == HttpMethod.GET || method == HttpMethod.PUT) && path.matches(".*/users/\\d+")) {
            return true;
        }
        if (path.matches(".*/tickets/") && method == HttpMethod.POST || // POST "/tickets/"
                path.matches(".*/tickets/\\d+$") || // GET "/tickets/{id}"
                path.matches(".*/tickets/by-user/\\d+$")) { // GET "/tickets/by-user/{userID}"
            return true;
        }

        return path.matches(".*/payments/") && method == HttpMethod.POST || // POST "/payments/"
                path.matches(".*/payments/\\d+$") || // GET "/payments/{id}"
                path.matches(".*/payments/by-user/\\d+$");
    }

    @Override
    public boolean requiresAdmin(ServerHttpRequest request) {
        String path = request.getPath().toString();
        HttpMethod method = request.getMethod();

        if ((method == HttpMethod.GET && path.matches(".*/users/$")) ||
                (method == HttpMethod.DELETE && path.matches(".*/users/\\d+"))) {
            return true;
        }
        if (path.matches(".*/(venues|locations)(/|$)(\\d+)?") && (method == HttpMethod.POST ||
                method == HttpMethod.PUT || method == HttpMethod.DELETE)) {
            return true;
        }
        if (path.matches(".*/tickets/by-event/\\d+$") || // GET "/tickets/by-event/{eventID}"
                path.matches(".*/tickets/$") && method == HttpMethod.GET) { // GET "/tickets/"
            return true;
        }
        if (path.matches(".*/payments/by-event/\\d+$") || // GET "/payments/by-event/{eventID}"
                path.matches(".*/payments/$") && method == HttpMethod.GET) { // GET "/payments/"
            return true;
        }if (path.matches(".*/notifications/confirmation") && method == HttpMethod.POST) {
            return true;
        }
        if (path.matches(".*/offertypes(/\\d+)?$") &&
                (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.DELETE)) {
            return true;
        }
        if (path.matches(".*/prices(/\\d+)?$") &&
                (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.DELETE || method == HttpMethod.GET)) {
            return true;
        }
        return path.matches(".*/events(/\\d+)?$") &&
                (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.DELETE || method == HttpMethod.PATCH) ||
                path.matches(".*/events/by-venue/\\d+$");
    }
}
