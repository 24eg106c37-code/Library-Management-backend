package com.library.dto;

import com.library.entity.LibraryTransaction;
import java.time.LocalDate;

public record TransactionResponse(Long id, Long bookId, String bookTitle, Long memberId,
                                 String memberName, LocalDate issueDate, LocalDate dueDate,
                                 LocalDate returnDate, String status) {
    public static TransactionResponse from(LibraryTransaction transaction) {
        return new TransactionResponse(transaction.getId(), transaction.getBook().getId(),
                transaction.getBook().getTitle(), transaction.getMember().getId(),
                transaction.getMember().getName(), transaction.getIssueDate(), transaction.getDueDate(),
                transaction.getReturnDate(), transaction.getStatus());
    }
}