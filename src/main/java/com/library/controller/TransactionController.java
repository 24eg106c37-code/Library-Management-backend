package com.library.controller;

import com.library.config.AuthInterceptor;
import com.library.dto.IssueRequest;
import com.library.dto.TransactionResponse;
import com.library.service.TokenStore;
import com.library.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactions;
    public TransactionController(TransactionService transactions) { this.transactions = transactions; }
    @PostMapping("/issue") @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse issue(@Valid @RequestBody IssueRequest request, HttpServletRequest servletRequest) {
        TokenStore.Principal principal = (TokenStore.Principal) servletRequest.getAttribute(AuthInterceptor.PRINCIPAL_ATTRIBUTE);
        return transactions.issue(request, principal == null ? null : principal.id());
    }
    @PostMapping("/return/{transactionId}")
    public TransactionResponse returnBook(@PathVariable @NonNull Long transactionId, HttpServletRequest servletRequest) {
        TokenStore.Principal principal = (TokenStore.Principal) servletRequest.getAttribute(AuthInterceptor.PRINCIPAL_ATTRIBUTE);
        return transactions.returnBook(transactionId, principal == null ? null : principal.id());
    }
    @GetMapping public List<TransactionResponse> all() { return transactions.all(); }
    @GetMapping("/member/{memberId}") public List<TransactionResponse> byMember(@PathVariable Long memberId) { return transactions.byMember(memberId); }
}