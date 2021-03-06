package com.jorge.springit.repository;

import com.jorge.springit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndActivationCode(String email, String activationCode);
}
