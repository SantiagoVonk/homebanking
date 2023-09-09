package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TransactionService {

    List<TransactionDTO> getTransactionsDTO();

    TransactionDTO getTransactionsDTO(@PathVariable Long id);

    void saveTransaction(Transaction transaction);
}
