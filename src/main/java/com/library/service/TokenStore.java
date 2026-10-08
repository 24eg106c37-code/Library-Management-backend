package com.library.service;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {
    private final Map<String, Principal> tokens = new ConcurrentHashMap<>();
    public String issue(Long id, String role) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new Principal(id, role));
        return token;
    }
    public Principal find(String token) { return tokens.get(token); }
    public record Principal(Long id, String role) {}
}