package com.library.controller;

import com.library.dto.BookRequest;
import com.library.entity.Book;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService books;
    public BookController(BookService books) { this.books = books; }
    @GetMapping public List<Book> all(@RequestParam(required = false) String keyword) { return books.all(keyword); }
    @GetMapping("/search") public List<Book> search(@RequestParam(defaultValue = "") String keyword) { return books.all(keyword); }
    @GetMapping("/{id}") public Book get(@PathVariable @NonNull Long id) { return books.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Book create(@Valid @RequestBody BookRequest request) { return books.create(request); }
    @PutMapping("/{id}") public Book update(@PathVariable @NonNull Long id, @Valid @RequestBody BookRequest request) { return books.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable @NonNull Long id) { books.delete(id); }
}