package com.dvwish.aitaskmanager.taskmanager.repository;


import com.dvwish.aitaskmanager.taskmanager.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByCognitoId(String cognitoId);
}