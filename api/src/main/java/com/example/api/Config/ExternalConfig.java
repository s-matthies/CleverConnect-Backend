package com.example.api.Config;

import com.example.api.Entitys.External;
import com.example.api.Entitys.Role;
import com.example.api.Repository.ExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class ExternalConfig {
    @Autowired
    //Configuration mit Repository verknüpfen
    private final ExternalRepository externalRepository;

    public ExternalConfig(ExternalRepository externalRepository) {
        this.externalRepository = externalRepository;
    }

    //wird beim Start des Programms ausgeführt
    @Bean
    CommandLineRunner initDataExtern(){
        return args -> {
            External external1 = new External(
                    "Missy",
                    "Elliot",
                    "missy.elliot@mail.de",
                    "qwertz",
                    LocalDate.of(2023, 11, 19),
                    Role.EXTERN,
                    false,
                    true,
                    "Big Data Company",
                    "01/2024-12/2024"
            );

            externalRepository.saveAll(
                    List.of(external1)
            );
        };
    }

}
