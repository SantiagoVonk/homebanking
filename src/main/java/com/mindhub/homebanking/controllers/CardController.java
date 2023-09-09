package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private CardService cardService;


    @GetMapping("/clients/cards")
    public List<CardDTO> getListCardDTO() {
        return cardService.getCardDTO();
    }

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCardDTO(Authentication authentication){
        ClientDTO clientDTO = new ClientDTO(clientService.findByEmail(authentication.getName()));
        return new ArrayList<>(clientDTO.getCards());
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType) {

        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if ( cardType != CardType.DEBIT && cardType != CardType.CREDIT) {
            return new ResponseEntity<>("Card type does not exist", HttpStatus.FORBIDDEN);
        }
        if (cardColor != CardColor.SILVER && cardColor != CardColor.GOLD && cardColor != CardColor.TITANIUM) {
            return new ResponseEntity<>("Card color does not exist", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
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
        int max = 999;
        int min = 100;
        card.setCvv(utilsService.getRandomNumber(max, min));
        card.setNumber(utilsService.getRandomNumberCard());
        clientService.saveClient(client);
        cardService.saveCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

