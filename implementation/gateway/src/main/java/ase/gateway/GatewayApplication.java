package ase.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ase.gateway.user.Admin;
import ase.gateway.user.Customer;
import ase.gateway.user.UserRepository;
import ase.gateway.user.Vendor;

@EnableJpaRepositories("ase.gateway.*")
@EntityScan("ase.gateway.*")
@ComponentScan(basePackages = { "ase.gateway.*" })
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {
			// Testcode, manipulate as necessary

			repository.save(new Vendor());
			repository.save(new Customer());
			repository.save(new Admin());

//			repository.deleteAll();

//			System.out.println("Users found with findAll():");
//			for (User customer : repository.findAll()) {
//				System.out.println(customer.toString());
//			}
		};
	}

}
