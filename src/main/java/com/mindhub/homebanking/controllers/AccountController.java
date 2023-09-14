package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UtilsService utilsService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
       return accountService.getAccount(id);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getCurrentAccounts(Authentication authentication) {
        return accountService.getCurrentAccounts(authentication);
    }


    @PostMapping(value = "/clients/current/accounts")
    public ResponseEntity<Object> getCurrentAccount(Authentication authentication) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Sorry, You have 3 Accounts", HttpStatus.FORBIDDEN);
        }
        Account account = new Account();
        account.setClient(client);
        account.setBalance(0.0);
        account.setCreationDate(LocalDateTime.now());

        int max = 99999999;
        int min = 1;
        account.setNumber("VIN00-" + utilsService.getRandomNumber(max, min));
        if (accountService.findByNumber(account.getNumber()) != null) {
            while (accountService.findByNumber(account.getNumber()) != null) {
                account.setNumber("VIN00-" + utilsService.getRandomNumber(max,min));
            }
        }
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

