package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {


    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }
/*
    @Autowired
    private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData( LoanRepository loanRepository, ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
  Client myClient01 = clientRepository.save(new Client("Melba", "Morel", passwordEncoder.encode("abc123456"), "14429865-9", "MEMO2030"));
            Account account0101 = accountRepository.save(new Account("VIN001", LocalDate.now(), 5000, myClient01));
			//Creamos los préstamos y los guardamos en el repositorio
			Loan hipotecario = loanRepository.save(new Loan("Hipotecario", 500000d, List.of(12, 24, 36, 48, 60)));
			Loan personal = loanRepository.save(new Loan("Personal", 100000d, List.of(6, 12, 24)));
			Loan automotriz = loanRepository.save(new Loan("Automotriz", 300000d, List.of(6, 12, 24, 36)));
		};
	} */
}
