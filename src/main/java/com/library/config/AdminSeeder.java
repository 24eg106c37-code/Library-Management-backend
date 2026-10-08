package com.library.config;

import com.library.entity.Admin;
import com.library.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminSeeder {
    @Bean
    CommandLineRunner seedAdmin(AdminRepository admins,
                                @Value("${library.admin.username}") String username,
                                @Value("${library.admin.password}") String password) {
        return args -> {
            if (admins.findByUsername(username).isEmpty()) admins.save(new Admin(username, new BCryptPasswordEncoder().encode(password)));
        };
    }
}