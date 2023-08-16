package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;


public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private int payments;
    private double amount;
    private Client client;
    private Loan loan;

    public ClientLoanDTO(ClientLoan clientLoan) { // clien nombre prestamo e id del prestamo debe devolver
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.payments = clientLoan.getPayments();
        this.amount = clientLoan.getAmounts();
        //this.client = clientLoan.getClient(); // borrar
        //this.loan = clientLoan.getLoan(); // borrar
    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public int getPayments() {
        return payments;
    }

    public double getAmount() {
        return amount;
    }

    public Client getClient() {
        return client;
    }

    public Loan getLoan() {
        return loan;
    }
}
