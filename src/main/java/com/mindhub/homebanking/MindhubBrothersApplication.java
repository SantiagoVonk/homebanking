package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MindhubBrothersApplication {


	public static void main(String[] args) {
		SpringApplication.run(MindhubBrothersApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner init(ClientService clientService,
								  AccountService accountService,
								  TransactionRepository transactionRepository,
								  LoanRepository loanRepository,
								  ClientLoanRepository clientLoanRepository,
								  CardRepository cardRepository) {

		return args -> {
			// create client Admin
			Client admin = new Client("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("0000"), ClientRol.ADMIN);
			clientService.saveClient(admin);

			// create client 1
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1111"), ClientRol.CLIENT );
			// save client 1
			clientService.saveClient(client1);

			// create account 1
			Account account1 = new Account("VIN001", 5000.0, LocalDateTime.now());
			account1.setClient(client1);
			// save account 1
			accountService.saveAccount(account1);

			Account account2 = new Account("VIN002", 7500.0, LocalDateTime.now().plusDays(1));
			account2.setClient(client1);
			accountService.saveAccount(account2);

			Transaction transaction1 = new Transaction(1000.0, "Salary", LocalDateTime.now(), TransactionType.CREDIT);
			transaction1.setAccount(account1);
			transactionRepository.save(transaction1);

			Transaction transaction2 = new Transaction(-50.0, "Dinner", LocalDateTime.now(), TransactionType.DEBIT );
			transaction2.setAccount(account1);
			transactionRepository.save(transaction2);

			Transaction transaction3 = new Transaction(1517.5, "Others", LocalDateTime.now(), TransactionType.CREDIT);
			transaction3.setAccount(account2);
			transactionRepository.save(transaction3);



			// create client 2
			Client client2 = new Client("Lionel", "Messi", "lionel@mindhub.com", passwordEncoder.encode("2222"), ClientRol.CLIENT);
			// save client 2
			clientService.saveClient(client2);

			Account account3 = new Account("VIN003", 12000.0, LocalDateTime.now()); // create account 3
			account3.setClient(client2); // set client2 in account 3
			accountService.saveAccount(account3);


			Loan loan1 = new Loan();
			loan1.setName("mortgageLoan");
			loan1.setMaxAmount(500000.0);
			loan1.setPayments(List.of(12, 24, 36, 48, 60));
			loanRepository.save(loan1);

			Loan loan2 = new Loan("personalLoan", 100000.0, List.of(6, 12, 24));
			loanRepository.save(loan2);

			Loan loan3 = new Loan("carLoan", 300000.0, List.of(6, 12, 24, 36));
			loanRepository.save(loan3);

			ClientLoan clientLoan = new ClientLoan();
			clientLoan.setPayments(60);
			clientLoan.setAmounts(400000.0);
			clientLoan.setClient(client1);
			clientLoan.setLoan(loan1);
			clientLoanRepository.save(clientLoan);

			ClientLoan clientLoan1 = new ClientLoan(12, 50000.0);
			clientLoan.setClient(client1);
			clientLoan.setLoan(loan2);
			clientLoanRepository.save(clientLoan1);

			//(String cardholder, CardType cardType, CardColor cardColor, String number, Short cvv, LocalDate fromDate, LocalDate thruDate)
			Card card = new Card();
			card.setClient(client1);
			card.setCardholder(client1.getFirstName() + " " + client1.getLastName());
			card.setCardType(CardType.DEBIT);
			card.setCardColor(CardColor.GOLD);
			card.setNumber("1234123412341234");
			card.setCvv(987);
			card.setFromDate(LocalDate.now());
			card.setThruDate(LocalDate.now().plusYears(5));
			cardRepository.save(card);

			Card card1 = new Card();
			card1.setClient(client1);
			card1.setCardholder(client1.getFirstName() + " " + client1.getLastName());
			card1.setCardType(CardType.CREDIT);
			card1.setCardColor(CardColor.TITANIUM);
			card1.setNumber("4321432143214321");
			card1.setCvv(963);
			card1.setFromDate(LocalDate.now());
			card1.setThruDate(LocalDate.now().plusYears(5));
			cardRepository.save(card1);
		};
	}


}
