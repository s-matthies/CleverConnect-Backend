package com.example.api.Repository;

import com.example.api.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//Repository greift auf Datenbank zu und mithilfe "extends" auf Repository von User zu
public interface UserRepository extends JpaRepository<User, Long> {

    // User wird mittels Email-Adresse in der Datenbank gefunden
    Optional <User> findByEmail(String email);
}
