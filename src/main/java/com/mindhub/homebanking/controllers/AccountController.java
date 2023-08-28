package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
public class AccountController  {

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

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> getCurrentAccount(Authentication authentication) {
        if (authentication.getName() != null) {
        ClientDTO clientDTO = new ClientDTO(clientRepository.findByEmail(authentication.getName()));
        String email = clientDTO.getEmail();
        Client client = clientRepository.findByEmail(email);
        if (client.getAccounts().size() <= 3) {
            Account account = new Account();
            account.setClient(client);
            account.setBalance(0.0);
            account.setCreationDate(LocalDateTime.now());
            //modificar a aleatorio y sin repeticion
            account.setNumber("VIN00" + client.getId());
                accountRepository.save(account);
                return new  ResponseEntity<>(HttpStatus.CREATED);
            }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    //return client.getAccounts();
    /*return new ResponseEntity<>(HttpStatus.CREATED);*/
}
