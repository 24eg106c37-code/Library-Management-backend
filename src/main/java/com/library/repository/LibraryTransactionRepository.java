package com.library.repository;

import com.library.entity.LibraryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibraryTransactionRepository extends JpaRepository<LibraryTransaction, Long> {
    List<LibraryTransaction> findAllByOrderByIssueDateDesc();
    List<LibraryTransaction> findByMemberIdOrderByIssueDateDesc(Long memberId);
    boolean existsByBookIdAndStatus(Long bookId, String status);
}