package com.library.service;

import com.library.dto.AuthResponse;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest;
import com.library.entity.Admin;
import com.library.entity.Member;
import com.library.repository.AdminRepository;
import com.library.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AdminRepository admins;
    private final MemberRepository members;
    private final TokenStore tokens;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public AuthService(AdminRepository admins, MemberRepository members, TokenStore tokens) {
        this.admins = admins; this.members = members; this.tokens = tokens;
    }
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (members.existsByEmailIgnoreCase(request.email())) throw new IllegalArgumentException("An account with this email already exists.");
        Member member = members.save(new Member(request.name().trim(), request.email().trim().toLowerCase(),
                encoder.encode(request.password()), request.phone() == null ? "" : request.phone().trim()));
        return response(member.getId(), member.getName(), "MEMBER");
    }
    public AuthResponse login(LoginRequest request) {
        if ("ADMIN".equalsIgnoreCase(request.role())) {
            Admin admin = admins.findByUsername(request.username()).orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
            if (!encoder.matches(request.password(), admin.getPasswordHash())) throw new IllegalArgumentException("Invalid username or password.");
            return response(admin.getId(), admin.getUsername(), "ADMIN");
        }
        Member member = members.findByEmailIgnoreCase(request.username()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if (!encoder.matches(request.password(), member.getPasswordHash())) throw new IllegalArgumentException("Invalid email or password.");
        return response(member.getId(), member.getName(), "MEMBER");
    }
    private AuthResponse response(Long id, String name, String role) { return new AuthResponse(tokens.issue(id, role), id, name, role); }
}