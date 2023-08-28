package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType) {
        if (authentication.getName() != null) {
            ClientDTO clientDTO = new ClientDTO(clientRepository.findByEmail(authentication.getName()));
            String email = clientDTO.getEmail();
            Client client = clientRepository.findByEmail(email);
            // modificar que sea menos de 3 del mismo tipo (CREDIT o DEBIT) usar .filter(cardType)
            if (client.getCards().size() > 3) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }else {
                Card card = new Card();
                card.setClient(client);
                card.setCardColor(cardColor);
                card.setCardType(cardType);
                card.setFromDate(LocalDate.now());
                card.setThruDate(LocalDate.now().plusYears(5));
                card.setCardholder(client.getFirstName() + " " + client.getLastName());
                //modificar despues por aleatorio el cvv y el number
                card.setCvv(455);
                card.setNumber("7555-7666-7888-7999");
                clientRepository.save(client);
                cardRepository.save(card);

            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
