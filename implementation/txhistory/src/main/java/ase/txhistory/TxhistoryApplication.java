package ase.txhistory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ase.txhistory.data.TransactionHistory;
import ase.txhistory.data.TransactionHistory.IdType;
import ase.txhistory.data.TransactionHistoryRepository;

@EnableJpaRepositories("ase.txhistory.*")
@EntityScan("ase.txhistory.*")
@ComponentScan(basePackages = { "ase.gateway.*" })
@SpringBootApplication
public class TxhistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxhistoryApplication.class, args);

	}

	@Bean
	public CommandLineRunner demo(TransactionHistoryRepository repository) {
		return (args) -> {
			// Testcode, manipulate as necessary
			System.out.println(TransactionHistory.generate(IdType.BUYER, repository, 1l));
		};
	}

}
