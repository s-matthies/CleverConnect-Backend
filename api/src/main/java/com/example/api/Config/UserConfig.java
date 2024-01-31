package com.example.api.Config;

import com.example.api.Entitys.Role;
import com.example.api.Entitys.User;
import com.example.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;


@Configuration
public class UserConfig {

/*

    // wir verknüpfen mit dem UserRepository
    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserConfig(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //wird beim Start des Programms ausgeführt

    @Bean
    CommandLineRunner commandLineRunner(){
        return args -> {

            User user1 = new User(
                    "Joern",
                    "Freiheit",
                    "Joern.Freiheit@HTW-Berlin.de",
                    bCryptPasswordEncoder.encode("passwort_Aber_sicher5!"),                    LocalDate.of(2023, 10, 15),
                    Role.ADMIN,
                    false,
                    true
            );

            User user2 = new User(
                    "Helena",
                    "Mihaljevic",
                    "Helena.Mihaljevic@HTW-Berlin.de",
                    bCryptPasswordEncoder.encode("Passwort123#"),                    LocalDate.of(2023, 11, 1),
                    Role.ADMIN,
                    false,
                    true
            );

            userRepository.saveAll(
                    List.of(user1, user2)
            );
        };
    }
    
 */

}
