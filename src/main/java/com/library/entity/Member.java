package com.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, unique = true, length = 160)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @Column(length = 30)
    private String phone;

    protected Member() {}
    public Member(String name, String email, String passwordHash, String phone) {
        this.name = name; this.email = email; this.passwordHash = passwordHash; this.phone = phone;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getPhone() { return phone; }
}