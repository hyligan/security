package com.goit.security.controllers;

import com.goit.security.entities.User;
import com.goit.security.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserCointroller {
  private final UserRepo userRepo;

  public UserCointroller(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @PostMapping("/add")
  public @ResponseBody User createUser(@RequestBody User user){
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    return userRepo.save(user);
  }
  @GetMapping
  public @ResponseBody Iterable<User> getAllUsers(){
    return userRepo.findAll();
  }
  
}
