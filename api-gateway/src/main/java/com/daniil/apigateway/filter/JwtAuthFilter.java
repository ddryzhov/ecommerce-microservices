package com.daniil.apigateway.filter;

import com.daniil.apigateway.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<Object> {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        super(Object.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();

            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.isValid(token)) {
                log.warn("Invalid JWT token for path: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String username = jwtUtil.getUsername(token);
            log.debug("Authenticated user: {} → {}", username, path);

            return chain.filter(
                    exchange.mutate()
                            .request(r -> r.header("X-User-Name", username))
                            .build()
            );
        };
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/actuator") ||
               path.startsWith("/api/auth");
    }
}
