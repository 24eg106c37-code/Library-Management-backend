package com.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books", indexes = @Index(name = "idx_book_isbn", columnList = "isbn"))
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 180)
    private String title;
    @Column(nullable = false, length = 140)
    private String author;
    @Column(nullable = false, length = 100)
    private String category;
    @Column(nullable = false, unique = true, length = 30)
    private String isbn;
    @Column(length = 140)
    private String publisher;
    private Integer publicationYear;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer availableQuantity;

    protected Book() {}
    public Book(String title, String author, String category, String isbn, String publisher,
                Integer publicationYear, Integer quantity) {
        this.title = title; this.author = author; this.category = category; this.isbn = isbn;
        this.publisher = publisher; this.publicationYear = publicationYear;
        this.quantity = quantity; this.availableQuantity = quantity;
    }
    public void update(String title, String author, String category, String isbn, String publisher,
                       Integer publicationYear, Integer quantity) {
        int issued = this.quantity - this.availableQuantity;
        if (quantity < issued) throw new IllegalArgumentException("Quantity cannot be less than the number of issued copies (" + issued + ").");
        this.title = title; this.author = author; this.category = category; this.isbn = isbn;
        this.publisher = publisher; this.publicationYear = publicationYear; this.quantity = quantity;
        this.availableQuantity = quantity - issued;
    }
    public void issueCopy() { if (availableQuantity <= 0) throw new IllegalStateException("No copies of this book are currently available."); availableQuantity--; }
    public void returnCopy() { if (availableQuantity < quantity) availableQuantity++; }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getIsbn() { return isbn; }
    public String getPublisher() { return publisher; }
    public Integer getPublicationYear() { return publicationYear; }
    public Integer getQuantity() { return quantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
}