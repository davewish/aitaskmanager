package com.dvwish.aitaskmanager.taskmanager.repository;



import com.dvwish.aitaskmanager.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCognitoId(String cognitoId);
}