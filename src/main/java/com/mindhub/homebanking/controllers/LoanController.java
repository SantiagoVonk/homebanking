package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
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
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoansDTO () {
        return loanService.getLoansDTO();
    }

    @GetMapping("/loans/{id}")
    public LoanDTO getLoanById(@PathVariable Long id) {
        return new LoanDTO(loanService.findById(id));
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> getLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        if (authentication.getName() == null || loanApplicationDTO == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
        Double amountLoan = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2);
        Loan loan = loanService.findById(loanApplicationDTO.getLoanId());

        if (loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 00) {
            return new ResponseEntity<>( "Amount or Payments Value = 0",HttpStatus.FORBIDDEN);
        }
        if (loanService.findById(loanApplicationDTO.getLoanId()) == null) {
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

        clientService.saveClient(client);
        transactionRepository.save(transaction);
        accountService.saveAccount(account);
        clientLoanRepository.save(clientLoan);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }
}
