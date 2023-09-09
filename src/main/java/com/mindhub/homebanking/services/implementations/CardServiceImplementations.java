package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplementations implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public List<CardDTO> getCardDTO() {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<CardDTO> getCurrentClientCardsDTO(Authentication authentication) {
        ClientDTO clientDTO = new ClientDTO(clientService.findByEmail(authentication.getName()));
        return new ArrayList<>(clientDTO.getCards());
    }

    @Override
    public CardDTO getCardDTObyNumber(String number) {
        return new CardDTO(cardRepository.findByNumber(number));
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
