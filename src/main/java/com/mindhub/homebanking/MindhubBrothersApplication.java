package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MindhubBrothersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindhubBrothersApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository,
								  AccountRepository accountRepository,
								  TransactionRepository transactionRepository,
								  LoanRepository loanRepository,
								  ClientLoanRepository clientLoanRepository,
								  CardRepository cardRepository) {
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

			Loan loan = new Loan();
			loan.setName("mortgageLoan");
			loan.setMaxAmount(500000.0);
			loan.setPayments(List.of(12, 24, 36, 48, 60));
			loanRepository.save(loan);

			Loan loan1 = new Loan("personalLoan", 100000.0, List.of(6, 12, 24));
			loanRepository.save(loan1);

			Loan loan2 = new Loan("carLoan", 300000.0, List.of(6, 12, 24, 36));
			loanRepository.save(loan2);

			ClientLoan clientLoan = new ClientLoan();
			clientLoan.setPayments(60);
			clientLoan.setAmounts(400000.0);
			clientLoan.setClient(client);
			clientLoan.setLoan(loan);
			clientLoanRepository.save(clientLoan);

			ClientLoan clientLoan1 = new ClientLoan(12, 50000.0, client, loan1);
			clientLoanRepository.save(clientLoan1);

			//(String cardholder, CardType cardType, CardColor cardColor, String number, Short cvv, LocalDate fromDate, LocalDate thruDate)
			Card card = new Card();
			card.setClient(client);
			card.setCardholder(client.getFirstName() + " " + client.getLastName());
			card.setCardType(CardType.DEBIT);
			card.setCardColor(CardColor.GOLD);
			card.setNumber("1234123412341234");
			card.setCvv(987);
			card.setFromDate(LocalDate.now());
			card.setThruDate(LocalDate.now().plusYears(5));
			cardRepository.save(card);

			Card card1 = new Card();
			card1.setClient(client);
			card1.setCardholder(client.getFirstName() + " " + client.getLastName());
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
