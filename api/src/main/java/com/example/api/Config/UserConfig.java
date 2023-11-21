package com.example.api.Config;

import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    // wir verknüpfen mit dem UserRepository
    @Autowired
    private final UserRepository userRepository;

    public UserConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //wird beim Start des Programms ausgeführt
    @Bean
    CommandLineRunner commandLineRunner(){
        return args -> {
            User user1 = new User(
                    "Anna",
                    "Bertram",
                    "meineMail@mail.de",
                    "12345",
                    Role.STUDENT
            );

            User user2 = new User(
                    "Berta",
                    "Cesar",
                    "mail@mail.de",
                    "passwort",
                    Role.ADMIN
            );

            userRepository.saveAll(
                    List.of(user1, user2)
            );
        };
    }
}
