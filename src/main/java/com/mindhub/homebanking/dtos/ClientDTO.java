package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName, lastName, email;
    private Set<AccountDTO> accountDTO;

    private Set<ClientLoanDTO> loans;
    public ClientDTO(Client client) {
        id = client.getId();
        firstName = client.getFirstName();
        lastName = client.getLastName();
        email = client.getEmail();
        accountDTO = client.getAccounts().stream().map(element -> new AccountDTO(element)).collect(Collectors.toSet());
        loans = client.getClientLoans().stream().map(element -> new ClientLoanDTO(element)).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accountDTO;
    }
    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }
}
