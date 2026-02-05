package com.TuWebYa.horno.auth.infra.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        String path = exchange.getRequest().getPath().value();

        // Skip authentication for public endpoints
        if (path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Authorization header found for path: {}", path);
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7);

        if (!jwtService.isValid(token)) {
            logger.warn("Invalid or expired token for path: {}", path);
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token"));
        }

        try {
            String userId = jwtService.extractUserId(token);
            String role = jwtService.extractRole(token);

            if (userId == null || role == null) {
                logger.warn("Token missing required claims (userId: {}, role: {})", userId, role);
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token claims"));
            }

            logger.debug("Authenticated user: {} with role: {} for path: {}", userId, role, path);

            var authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority(role))
            );

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        } catch (Exception e) {
            logger.error("Error processing JWT token for path: {}", path, e);
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to process token: " + e.getMessage()));
        }
    }
}