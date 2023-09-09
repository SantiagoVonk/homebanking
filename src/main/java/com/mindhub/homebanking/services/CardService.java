package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {

    List<CardDTO> getCardDTO();

    List<CardDTO> getCurrentClientCardsDTO(Authentication authentication);

    CardDTO getCardDTObyNumber(String number);
    void saveCard(Card card);
}
