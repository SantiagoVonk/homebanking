package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController extends Utils{

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
       return new AccountDTO(accountRepository.findById(id).orElse(null));
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication) {
        ClientDTO clientDTO = new ClientDTO(clientRepository.findByEmail(authentication.getName()));
        return clientDTO.getAccounts().stream().collect(Collectors.toList());
        //return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }


    @PostMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> getCurrentAccount(Authentication authentication) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Sorry, You have 3 Accounts", HttpStatus.FORBIDDEN);
        }
        Account account = new Account();
        account.setClient(client);
        account.setBalance(0.0);
        account.setCreationDate(LocalDateTime.now());
        //numero cuenta aleatorio (ok) y sin repeticion falta
        account.setNumber("VIN00-" + getRandomNumber(99999999, 1));
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

