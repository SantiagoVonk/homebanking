package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> addTransactions(Authentication authentication,
                                                  @RequestParam Double amount,
                                                  @RequestParam String description,
                                                  @RequestParam String fromAccountNumber,
                                                  @RequestParam String toAccountNumber) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (amount == null || description.isBlank() || fromAccountNumber.isBlank() || toAccountNumber.isBlank()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(fromAccountNumber) == null || accountRepository.findByNumber(toAccountNumber) == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (accountRepository.findByNumber(fromAccountNumber).getBalance() < amount) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        // continuar con las verificaciones

        Transaction transactionOriginAccount = new Transaction(amount, description +" to "+ toAccountNumber, LocalDateTime.now(), TransactionType.DEBIT, accountRepository.findByNumber(fromAccountNumber));
        accountRepository.findByNumber(fromAccountNumber).setBalance(accountRepository.findByNumber(fromAccountNumber).getBalance() - amount);
        accountRepository.findByNumber(fromAccountNumber).addTransaction(transactionOriginAccount);
        transactionRepository.save(transactionOriginAccount);
        accountRepository.save(accountRepository.findByNumber(fromAccountNumber));

        Transaction transactionTargetAccount = new Transaction(amount, description +" from "+ fromAccountNumber, LocalDateTime.now(), TransactionType.CREDIT, accountRepository.findByNumber(toAccountNumber));
        accountRepository.findByNumber(toAccountNumber).setBalance(accountRepository.findByNumber(toAccountNumber).getBalance() + amount);
        accountRepository.findByNumber(toAccountNumber).addTransaction(transactionTargetAccount);
        transactionRepository.save(transactionTargetAccount);
        accountRepository.save(accountRepository.findByNumber(toAccountNumber));

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
