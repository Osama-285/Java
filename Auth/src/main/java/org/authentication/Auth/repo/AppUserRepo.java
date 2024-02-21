package org.authentication.Auth.repo;

import java.util.Optional;

import org.authentication.Auth.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepo extends MongoRepository<AppUser, String>{
    boolean existsByUsername(String username);

    Optional<AppUser> findByUsername(String username);
} 
