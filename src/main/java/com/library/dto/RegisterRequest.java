package com.library.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank @Size(max = 120) String name,
                              @NotBlank @Email @Size(max = 160) String email,
                              @NotBlank @Size(min = 8, max = 72) String password,
                              @Size(max = 30) String phone) {}