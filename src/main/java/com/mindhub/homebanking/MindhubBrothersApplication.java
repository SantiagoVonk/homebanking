package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MindhubBrothersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindhubBrothersApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository) {
		return args -> {
			Client client = new Client();
			client.setFirstName("Melba");
			client.setLastName("Morel");
			client.setEmail("melba@mindhub.com");
			clientRepository.save(client);

			Client client1 = new Client("Lionel", "Messi", "lionel@mindhub.com");
			clientRepository.save(client1);

			Client client2 = new Client();
			client2.setFirstName("Cristiano");
			client2.setLastName("Ronaldo");
			client2.setEmail("cristiano@mindhub.com");
			clientRepository.save(client2);
		};
	}
}
