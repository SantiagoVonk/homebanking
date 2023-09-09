package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

public interface AccountService {

    List<AccountDTO> getAccountsDTO();

    AccountDTO getAccount(@PathVariable Long id);

    Set<AccountDTO> getCurrentAccounts(Authentication authentication);

    Account findByNumber(String number);

    void saveAccount(Account account);
}
