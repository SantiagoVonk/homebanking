package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientRol;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UtilsService utilsService;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
       return clientService.getClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
            return clientService.getCurrentClientDTO(authentication);
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> registerClient(@RequestParam String firstName,
                                                 @RequestParam String lastName,
                                                 @RequestParam String email,
                                                 @RequestParam String password) {

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            if (firstName.isBlank()) {
                return new ResponseEntity<>("Complete first name field", HttpStatus.FORBIDDEN);
            }
            if (lastName.isBlank()) {
                return new ResponseEntity<>("Complete last name field", HttpStatus.FORBIDDEN);
            }
            if (email.isBlank()) {
                return new ResponseEntity<>("Complete the email field", HttpStatus.FORBIDDEN);
            }
            if (password.isBlank()) {
                return new ResponseEntity<>("Complete the password field", HttpStatus.FORBIDDEN);
            }
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Email is already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), ClientRol.CLIENT);
        Account account = new Account();
        account.setClient(client);
        account.setBalance(0.0);
        account.setCreationDate(LocalDateTime.now());
        account.setNumber("VIN00-" + utilsService.getRandomNumber(99999999, 1));
        clientService.saveClient(client);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
