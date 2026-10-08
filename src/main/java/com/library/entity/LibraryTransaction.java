package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "library_transactions")
public class LibraryTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @Column(nullable = false)
    private LocalDate issueDate;
    @Column(nullable = false)
    private LocalDate dueDate;
    private LocalDate returnDate;
    @Column(nullable = false, length = 20)
    private String status;

    protected LibraryTransaction() {}
    public LibraryTransaction(Book book, Member member, LocalDate issueDate, LocalDate dueDate) {
        this.book = book; this.member = member; this.issueDate = issueDate; this.dueDate = dueDate; this.status = "ISSUED";
    }
    public void markReturned(LocalDate date) { this.returnDate = date; this.status = "RETURNED"; }
    public Long getId() { return id; }
    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
}