package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController extends Utils {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        //falta controlar si cardType y cardColor no estan vacios, que no me deja :\

        Client client = clientRepository.findByEmail(authentication.getName());
        Set clientCardType = client.getCards().stream().filter(card -> card.getCardType().equals(cardType)).collect(Collectors.toSet());
        if (clientCardType.size() >= 3) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Card card = new Card();
        card.setClient(client);
        card.setCardColor(cardColor);
        card.setCardType(cardType);
        card.setFromDate(LocalDate.now());
        card.setThruDate(LocalDate.now().plusYears(5));
        card.setCardholder(client.getFirstName() + " " + client.getLastName());
        card.setCvv(getRandomNumber(999, 100));
        card.setNumber( getRandomNumber(9999,1000) + "-" +
                        getRandomNumber(9999,1000) + "-" +
                        getRandomNumber(9999,1000) + "-" +
                        getRandomNumber(9999,1000));
        clientRepository.save(client);
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

