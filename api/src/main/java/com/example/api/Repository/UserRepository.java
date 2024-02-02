package com.example.api.Repository;

import com.example.api.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository für die Speicherung von Usern
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // User mittels E-Mail in der DB finden
    Optional <User> findByEmailIgnoreCase(String email);

}
