package com.goit.security.repo;

import com.goit.security.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User,Long> {
  Optional<User> findByEmail(String email);

  
  Optional<User> findByGoogleId(String aLong);
}
