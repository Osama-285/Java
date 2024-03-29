package org.practice.test.repository;

import java.util.Optional;

import org.practice.test.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepo extends JpaRepository<AppUser, String> {

    boolean existsByUsername(String username);

    Optional<AppUser> findByUsername(String username);
} 
