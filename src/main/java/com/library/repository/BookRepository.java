package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbnIgnoreCase(String isbn);
    @Query("select b from Book b where lower(b.title) like lower(concat('%', :keyword, '%')) or lower(b.author) like lower(concat('%', :keyword, '%')) or lower(b.category) like lower(concat('%', :keyword, '%')) or lower(b.isbn) like lower(concat('%', :keyword, '%'))")
    List<Book> search(@Param("keyword") String keyword);
}