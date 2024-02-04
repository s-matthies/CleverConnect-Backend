package com.example.api.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users API")
                        .description("Diese API ermöglicht die Verwaltung von Benutzer_innen (sowohl Studierende " +
                                "als auch externe Personen). Sie bietet Endpunkte zum Erstellen, Abrufen, Aktualisieren," +
                                "Anmelden, Abmelden und Löschen von Benutzer_innen. " +
                                "Darüber hinaus unterstützt sie spezielle Funktionen wie das Ändern von Passwörtern. " +
                                "Die API ist mit Spring Boot und Springdoc OpenAPI implementiert.")
                        .version("1.0"));
    }
}
