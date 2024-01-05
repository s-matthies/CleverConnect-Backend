package com.example.api.Config;

import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.External;
import com.example.api.Entitys.Role;
import com.example.api.Repository.ExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ExternalConfig {
    @Autowired
    //Configuration mit Repository verknüpfen
    private final ExternalRepository externalRepository;

    public ExternalConfig(ExternalRepository externalRepository) {
        this.externalRepository = externalRepository;
    }

    External external1;
/*
    //wird beim Start des Programms ausgeführt

    /*
    @Bean
    CommandLineRunner initDataExtern(){
        return args -> {
            external1 = new External(
                    "Missy",
                    "Elliot",
                    "missy.elliot@mail.de",
                    "qwertz",
                    LocalDate.of(2023, 11, 19),
                    Role.EXTERN,
                    false,
                    true,
                    "Big Data Company",
                    LocalDate.of(2023, 1, 14), // Startdatum
                    LocalDate.of(2023, 12, 14), // Enddatum
                    "Ich bin aufgeschlossen, kann nicht nur singen, sondern liebe auch IT",
                    "Datenbanken, Programmierung, Webtechnologien",
                    List.of( new BachelorSubject (
                            "spannendes thema1",
                            "tolles Thema, echt wirklich",
                            LocalDate.of(2023, 12, 27),
                            external1
                    ),
                             new BachelorSubject(
                    "THEMA 2",
                    "great",
                    LocalDate.of(2023, 12, 27),
                    external1
                    ))



            );

            externalRepository.saveAll(
                    List.of(external1)
            );
        };
    }*/

}
