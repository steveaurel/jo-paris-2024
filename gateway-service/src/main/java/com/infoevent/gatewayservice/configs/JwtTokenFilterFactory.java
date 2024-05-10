package com.infoevent.gatewayservice.configs;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFilterFactory extends AbstractGatewayFilterFactory<JwtTokenFilterFactory.Config> {

    private final JwtTokenFilter jwtTokenFilter;

    public JwtTokenFilterFactory(JwtTokenFilter jwtTokenFilter) {
        super(Config.class);
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return jwtTokenFilter::filter;
    }

    public static class Config {
    }
}
