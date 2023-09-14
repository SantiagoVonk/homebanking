package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactionsDTO() {
        return transactionService.getTransactionsDTO();
    }

    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionsDTO(id);
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> addTransactions(Authentication authentication,
                                                  @RequestParam Double amount,
                                                  @RequestParam String description,
                                                  @RequestParam String fromAccountNumber,
                                                  @RequestParam String toAccountNumber) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>("Client is not authenticated", HttpStatus.FORBIDDEN);
        }
        if (amount == 0.0 || description.isBlank() || fromAccountNumber.isBlank() || toAccountNumber.isBlank()) {
            if (amount == 0.0) {
                return new ResponseEntity<>("Amount cannot be equal to zero",HttpStatus.FORBIDDEN);
            }
            if (description.isBlank()) {
                return new ResponseEntity<>("Description cannot be empty",HttpStatus.FORBIDDEN);
            }
            if (fromAccountNumber.isBlank()) {
                return new ResponseEntity<>("Origin account cannot be empty",HttpStatus.FORBIDDEN);
            }
            if (toAccountNumber.isBlank()) {
                return new ResponseEntity<>("Target account cannot be empty",HttpStatus.FORBIDDEN);
            }
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Origin account cannot be the same as destination account",HttpStatus.FORBIDDEN);
        }
        if (accountService.findByNumber(fromAccountNumber) == null) {
            return new ResponseEntity<>("Origin account not found",HttpStatus.FORBIDDEN);
        }
        if ( accountService.findByNumber(toAccountNumber) == null) {
            return new ResponseEntity<>("Target account not found",HttpStatus.FORBIDDEN);
        }

        if (accountService.findByNumber(fromAccountNumber).getBalance() < amount) {
            return new ResponseEntity<>("The amount to be transferred cannot be greater than the amount in the account",HttpStatus.FORBIDDEN);
        }

        Transaction transactionOriginAccount = new Transaction(amount, description +" to "+ toAccountNumber, LocalDateTime.now(), TransactionType.DEBIT);
        transactionOriginAccount.setAccount(accountService.findByNumber(fromAccountNumber));
        accountService.findByNumber(fromAccountNumber).setBalance(accountService.findByNumber(fromAccountNumber).getBalance() - amount);
        accountService.findByNumber(fromAccountNumber).addTransaction(transactionOriginAccount);
        transactionService.saveTransaction(transactionOriginAccount);
        accountService.saveAccount(accountService.findByNumber(fromAccountNumber));

        Transaction transactionTargetAccount = new Transaction(amount, description +" from "+ fromAccountNumber, LocalDateTime.now(), TransactionType.CREDIT);
        transactionTargetAccount.setAccount(accountService.findByNumber(toAccountNumber));
        accountService.findByNumber(toAccountNumber).setBalance(accountService.findByNumber(toAccountNumber).getBalance() + amount);
        accountService.findByNumber(toAccountNumber).addTransaction(transactionTargetAccount);
        transactionService.saveTransaction(transactionTargetAccount);
        accountService.saveAccount(accountService.findByNumber(toAccountNumber));

        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }
}
