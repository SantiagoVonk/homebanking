package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;

public class AccountDTO {
    private Long id;
    private String number;
    private Double balance;
    private LocalDateTime date;

    public AccountDTO(Account account) {
        id = account.getId();
        number = account.getNumber();
        balance = account.getBalance();
        date = account.getCreationDate();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Double getBalance() {
        return balance;
    }

    public LocalDateTime getCreationDate() {
        return date;
    }
}
