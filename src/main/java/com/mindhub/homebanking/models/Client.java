package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name= "native", strategy = "native")
    private Long id;
    private String firstName, lastName, email, password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    private ClientRol clientRol;
    public Client() {
    }

    //importante: no utilizar los metodos constructores para agregar las propiedades relacionales
    // ej: accounts, cards, clientLoans, para eso utilizar los metodos creados especificamente (addAccounts, addCards, addClientLoans)
    public Client(String firstName, String lastName, String email, String password, ClientRol clientRol) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.clientRol = clientRol;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccounts(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public List<Loan> getLoans() {
        return clientLoans.stream().map(element -> element.getLoan()).collect(Collectors.toList());
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }
    public void addCards(Card card) {
        card.setClient(this);
        cards.add(card);
    }

    public ClientRol getClientRol() {
        return clientRol;
    }

    public void setClientRol(ClientRol clientRol) {
        this.clientRol = clientRol;
    }
}
