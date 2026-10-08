package com.library.config;

import com.library.service.TokenStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String PRINCIPAL_ATTRIBUTE = "libraryPrincipal";
    private final TokenStore tokens;
    public AuthInterceptor(TokenStore tokens) { this.tokens = tokens; }
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String path = request.getRequestURI();
        String method = request.getMethod();
        boolean adminOnly = path.equals("/api/members") || path.equals("/api/transactions") ||
                (path.startsWith("/api/books") && !method.equals("GET"));
        boolean memberTransaction = path.equals("/api/transactions/issue") || path.matches("/api/transactions/return/\\d+") || path.matches("/api/transactions/member/\\d+");
        if (!adminOnly && !memberTransaction) return true;
        String header = request.getHeader("Authorization");
        TokenStore.Principal principal = header != null && header.startsWith("Bearer ") ? tokens.find(header.substring(7)) : null;
        String requiredRole = adminOnly ? "ADMIN" : "MEMBER";
        if (principal == null || !requiredRole.equals(principal.role())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Please sign in with an authorized account.\"}");
            return false;
        }
        request.setAttribute(PRINCIPAL_ATTRIBUTE, principal);
        if (path.matches("/api/transactions/member/\\d+") && !path.endsWith("/" + principal.id())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"You can only view your own transactions.\"}");
            return false;
        }
        return true;
    }
}