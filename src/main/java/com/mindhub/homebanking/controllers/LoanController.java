package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoansDTO () {
        return loanRepository.findAll().stream().map(loan -> new  LoanDTO(loan)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> getLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        if (authentication.getName() == null || loanApplicationDTO == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        Double amountLoan = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2);
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);

        if (loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 00) {
            return new ResponseEntity<>( "Amount or Payments Value = 0",HttpStatus.FORBIDDEN);
        }
        if (!loanRepository.findById(loanApplicationDTO.getLoanId()).isPresent()) {
            return new ResponseEntity<>("Loan does not exist ", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>( "Amount requested exceeds the amount of the loan",HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Target account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Destination account does not belong to the customer", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getPayments(), amountLoan);
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), loan.getName() + " " + "loan aproved", LocalDateTime.now(), TransactionType.CREDIT);
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        account.addTransaction(transaction);

        clientRepository.save(client);
        transactionRepository.save(transaction);
        accountRepository.save(account);
        clientLoanRepository.save(clientLoan);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }
}
