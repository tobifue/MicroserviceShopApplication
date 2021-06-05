package ase.gateway.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ase.gateway.user.User;
import ase.gateway.user.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	// get User
	@GetMapping("/{id}")
	public User findById(@PathVariable("id") Long id) {
		return userRepository.findByUserId(id);
	}

	// add User
	@PostMapping("/add")
	public User addOrReturnUser(@RequestBody User newUser) {
		Optional<User> user = userRepository.findById(newUser.getId());
		if (user.isPresent()) {
			return user.get();
		}
		return userRepository.save(newUser);
	}

	// delete User
	@RequestMapping(value = "/{id}")
	@DeleteMapping
	public String delete(@PathVariable("id") Long id) {
		userRepository.deleteById(id);
		return "Delete successfull";
	}
}
