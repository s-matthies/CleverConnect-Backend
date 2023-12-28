package com.example.api.Config;

import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
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
                    "meineMail@Student.HTW-Berlin.de",
                    "12345-Hundert",
                    LocalDate.of(2023, 10, 1),
                    Role.STUDENT,
                    false,
                    true
            );

            User user2 = new User(
                    "Jörn",
                    "Freiheit",
                    "Joern.Freiheit@HTW-Berlin.de",
                    "passwort_Aber_sicher!",
                    LocalDate.of(2023, 10, 15),
                    Role.ADMIN,
                    false,
                    true
            );

            User user3 = new User(
                    "Helena",
                    "Mihaljevic",
                    "Helena.Mihaljevic@HTW-Berlin.de",
                    "passwort123#",
                    LocalDate.of(2023, 11, 1),
                    Role.ADMIN,
                    false,
                    true
            );

            userRepository.saveAll(
                    List.of(user1, user2)
            );
        };
    }
}
