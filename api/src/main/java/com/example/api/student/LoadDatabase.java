package com.example.api.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(StudentRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Student(
                    "Missy",
                    "Elliot",
                    "missyelliot@testmail.com",
                    "12345")));
            log.info("Preloading " + repository.save(new Student(
                    "Beyonce",
                    "Knowles",
                    "b.knowles@testmail.com",
                    "qwertz")));
        };
    }
}
