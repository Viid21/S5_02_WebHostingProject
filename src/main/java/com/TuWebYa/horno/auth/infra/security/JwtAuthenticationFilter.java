package com.TuWebYa.horno.auth.infra.security;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final SecurityContextService securityContextService;

    public JwtAuthenticationFilter(JwtService jwtService, SecurityContextService securityContextService) {
        this.jwtService = jwtService;
        this.securityContextService = securityContextService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        if (!jwtService.isValid(token)) {
            return chain.filter(exchange);
        }

        String userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);

        return securityContextService.saveAuthentication(userId, role)
                .then(chain.filter(exchange));
    }
}