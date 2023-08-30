package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientRol;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController extends Utils {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
       return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication) {
            return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> registerClient(@RequestParam String firstName,
                                                 @RequestParam String lastName,
                                                 @RequestParam String email,
                                                 @RequestParam String password) {

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing DATA, please complete", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Email is already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password), ClientRol.CLIENT);

        Account account = new Account();
        account.setClient(client);
        account.setBalance(0.0);
        account.setCreationDate(LocalDateTime.now());
        //numero cuenta aleatorio (ok) y sin repeticion falta
        account.setNumber("VIN00-" + getRandomNumber(99999999, 1));
        clientRepository.save(client);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    //aca me perdi xD
    /*@PostMapping("/login")
    public ResponseEntity<Object> loginClient(@RequestParam String email, @RequestParam String password) {
        if (email.isBlank() || password.isBlank())  {
            return new ResponseEntity<>("Check Your Data",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }*/

}
