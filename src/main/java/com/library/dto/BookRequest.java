package com.library.dto;

import jakarta.validation.constraints.*;

public record BookRequest(@NotBlank @Size(max = 180) String title,
                          @NotBlank @Size(max = 140) String author,
                          @NotBlank @Size(max = 100) String category,
                          @NotBlank @Size(max = 30) String isbn,
                          @Size(max = 140) String publisher,
                          @Min(1400) @Max(2100) Integer publicationYear,
                          @NotNull @Min(1) @Max(10000) Integer quantity) {}