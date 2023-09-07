package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private Long loanId;
    private Double amount;
    private int payments;

    private String toAccountNumber;

    public LoanApplicationDTO() {}

    public LoanApplicationDTO(Long id , Double amount, int payments, String toAccountNumber) {
        this.loanId = id;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
