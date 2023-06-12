package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> addTransaction(Authentication authentication, @RequestParam String fromAccountNumber,
                                                 @RequestParam String toAccountNumber, @RequestParam double amount,
                                                 @RequestParam String description) {
        return transactionService.addTransaction(authentication, fromAccountNumber, toAccountNumber, amount, description);
    }
}
