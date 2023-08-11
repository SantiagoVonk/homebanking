package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class MindhubBrothersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindhubBrothersApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return args -> {
			Client client = new Client();
			client.setFirstName("Melba");
			client.setLastName("Morel");
			client.setEmail("melba@mindhub.com");
			clientRepository.save(client);

			Account account = new Account();
			account.setNumber("VIN001");
			account.setCreationDate(LocalDateTime.now());
			account.setBalance(5000.0);
			account.setClient(client);
			accountRepository.save(account);

			Account account1 = new Account();
			account1.setNumber("VIN002");
			account1.setCreationDate(LocalDateTime.now().plusDays(1));
			account1.setBalance(7500.0);
			account1.setClient(client);
			accountRepository.save(account1);


			Client client1 = new Client("Lionel", "Messi", "lionel@mindhub.com");
			client1.setFirstName("Lionel");
			client1.setLastName("Messi");
			client1.setEmail("lionel@mindhub.com");
			clientRepository.save(client1);

			Account account2 = new Account();
			account2.setNumber("VIN003");
			account2.setCreationDate(LocalDateTime.now());
			account2.setBalance(120000.0);
			account2.setClient(client1);
			accountRepository.save(account2);

			Transaction transaction = new Transaction();
			transaction.setAmount(1000.0);
			transaction.setDescription("Salary");
			transaction.setDate(LocalDateTime.now());
			transaction.setType(TransactionType.CREDIT);
			transaction.setAccount(account);
			transactionRepository.save(transaction);

			Transaction transaction1 = new Transaction(-50.0, "Dinner", LocalDateTime.now(), TransactionType.DEBIT, account );
			transactionRepository.save(transaction1);

			Transaction transaction2 = new Transaction(1517.5, "Others", LocalDateTime.now(), TransactionType.CREDIT, account1);
			transactionRepository.save(transaction2);

		};
	}
}
