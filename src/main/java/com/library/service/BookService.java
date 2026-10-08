package com.library.service;

import com.library.dto.BookRequest;
import com.library.entity.Book;
import com.library.exception.NotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.LibraryTransactionRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class BookService {
    private final BookRepository books;
    private final LibraryTransactionRepository transactions;
    public BookService(BookRepository books, LibraryTransactionRepository transactions) { this.books = books; this.transactions = transactions; }
    public List<Book> all(String keyword) { return keyword == null || keyword.isBlank() ? books.findAll() : books.search(keyword.trim()); }
    public @NonNull Book get(@NonNull Long id) { return Objects.requireNonNull(books.findById(id).orElseThrow(() -> new NotFoundException("Book not found."))); }
    public Book create(BookRequest request) {
        if (books.existsByIsbnIgnoreCase(request.isbn())) throw new IllegalArgumentException("A book with this ISBN already exists.");
        return books.save(new Book(request.title().trim(), request.author().trim(), request.category().trim(), request.isbn().trim(),
                request.publisher() == null ? "" : request.publisher().trim(), request.publicationYear(), request.quantity()));
    }
    @Transactional
    public Book update(@NonNull Long id, BookRequest request) {
        Book book = get(id);
        boolean isbnUsedByOtherBook = books.findAll().stream().anyMatch(existing -> !existing.getId().equals(id) && existing.getIsbn().equalsIgnoreCase(request.isbn()));
        if (isbnUsedByOtherBook) throw new IllegalArgumentException("A book with this ISBN already exists.");
        book.update(request.title().trim(), request.author().trim(), request.category().trim(), request.isbn().trim(),
                request.publisher() == null ? "" : request.publisher().trim(), request.publicationYear(), request.quantity());
        return book;
    }
    @Transactional
    public void delete(@NonNull Long id) {
        Book book = get(id);
        if (transactions.existsByBookIdAndStatus(id, "ISSUED")) throw new IllegalStateException("This book has an active issue and cannot be deleted.");
        books.delete(book);
    }
}