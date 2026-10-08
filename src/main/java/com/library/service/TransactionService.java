package com.library.service;

import com.library.dto.IssueRequest;
import com.library.dto.TransactionResponse;
import com.library.entity.Book;
import com.library.entity.LibraryTransaction;
import com.library.entity.Member;
import com.library.exception.NotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.LibraryTransactionRepository;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final LibraryTransactionRepository transactions;
    private final BookRepository books;
    private final MemberRepository members;
    private final int dueDays;
    public TransactionService(LibraryTransactionRepository transactions, BookRepository books, MemberRepository members,
                              @Value("${library.due-days:14}") int dueDays) {
        this.transactions = transactions; this.books = books; this.members = members; this.dueDays = dueDays;
    }
    @Transactional
    public TransactionResponse issue(IssueRequest request, Long authenticatedMemberId) {
        Long memberId = authenticatedMemberId == null ? request.memberId() : authenticatedMemberId;
        if (memberId == null) throw new IllegalArgumentException("Member ID is required.");
        Member member = members.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found."));
        Book book = books.findById(request.bookId()).orElseThrow(() -> new NotFoundException("Book not found."));
        book.issueCopy();
        LocalDate today = LocalDate.now();
        return TransactionResponse.from(transactions.save(new LibraryTransaction(book, member, today, today.plusDays(dueDays))));
    }
    @Transactional
    public TransactionResponse returnBook(@NonNull Long transactionId, Long memberId) {
        LibraryTransaction transaction = transactions.findById(transactionId).orElseThrow(() -> new NotFoundException("Transaction not found."));
        if (memberId != null && !transaction.getMember().getId().equals(memberId)) throw new IllegalArgumentException("This transaction does not belong to your account.");
        if (!"ISSUED".equals(transaction.getStatus())) throw new IllegalStateException("This book has already been returned.");
        transaction.markReturned(LocalDate.now());
        transaction.getBook().returnCopy();
        return TransactionResponse.from(transaction);
    }
    @Transactional(readOnly = true)
    public List<TransactionResponse> all() { return transactions.findAllByOrderByIssueDateDesc().stream().map(TransactionResponse::from).toList(); }
    @Transactional(readOnly = true)
    public List<TransactionResponse> byMember(Long memberId) { return transactions.findByMemberIdOrderByIssueDateDesc(memberId).stream().map(TransactionResponse::from).toList(); }
}