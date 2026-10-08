package com.library.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

public record IssueRequest(@NotNull @NonNull Long bookId, Long memberId) {}