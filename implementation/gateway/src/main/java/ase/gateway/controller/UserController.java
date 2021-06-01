package ase.gateway.controller;

import ase.gateway.user.User;
import ase.gateway.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //get User
    @GetMapping("/{id}")
    public User findById(@PathVariable("id")Long id){
        return userRepository.findByUserId(id);
    }

    //add User
    @PostMapping("/add")
    public User saveDepartment(@RequestBody User new_user){
        User newUser = new User(new_user.getEmail());
        return userRepository.save(newUser);
    }

    //delete User
    @RequestMapping(value = "/{id}")
    @DeleteMapping
    public String delete(@PathVariable("id") Long id) {

        userRepository.deleteById(id);
        return "Delete successfull";
    }
}
