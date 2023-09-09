package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getLoansDTO();

    Loan findById(@PathVariable Long id);

    void saveLoan(Loan loan);
}
