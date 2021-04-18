package ase.txhistory;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ase.txhistory.data.Transaction;
import ase.txhistory.data.TransactionHistory;
import ase.txhistory.data.TransactionHistory.IdType;
import ase.txhistory.data.TransactionHistoryRepository;

@EnableJpaRepositories("ase.txhistory.*")
@EntityScan("ase.txhistory.*")
@ComponentScan(basePackages = { "ase.txhistory.*" })
@RestController
@SpringBootApplication
public class TxhistoryApplication {

	private TransactionHistoryRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(TxhistoryApplication.class, args);
	}

	@RequestMapping(value = "/generate/{type}/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable String type, @PathVariable Long userid) {
		try {
			return TransactionHistory.generate(IdType.parse(type), repository, userid);
		} catch (InvalidParameterException e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "/generate/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public String generateHistory(@PathVariable Long userid) {
		return TransactionHistory.generate(repository, userid);
	}

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	@ResponseBody
	public List<Transaction> generateHistory() {
		return repository.findAll();
	}

	@PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Transaction addTransaction(@RequestBody Transaction transaction) {
		return repository.save(transaction);
	}

	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public void printRepositoryToConsole() {
		System.out.println("All transactions in the repository");
		List<Transaction> txs = repository.findAll();
		for (Transaction t : txs)
			System.out.println(t);
	}

	@RequestMapping(value = "/clearAll", method = RequestMethod.GET)
	public String clearAllTransactions() {
		repository.deleteAll();
		return "Cleared all transactions";
	}

	@Bean
	public CommandLineRunner loadRepository(TransactionHistoryRepository repository) {
		return (args) -> {
			this.repository = repository;
			printRepositoryToConsole();
		};
	}

}