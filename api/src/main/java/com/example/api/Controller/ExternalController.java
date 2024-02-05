package com.example.api.Controller;

import com.example.api.DTO.ExternalDTO;
import com.example.api.Request.ExternalRequest;
import com.example.api.Service.ExternalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller für die externen Personen (Zweitbetreuer*innen)
 */
@RestController
@RequestMapping("/external")
public class ExternalController {
    @Autowired
    private final ExternalService externalService;

    /**
     * Konstruktor für den ExternalController
     * @param externalService Der Service für die externen Personen (Zweitbetreuer*innen)
     */
    public ExternalController(ExternalService externalService) {
        this.externalService = externalService;
    }


    /**
     * Methode für das Registrieren einer externen Person (Zweitbetreuer*in)
     *
     * @param externalRequest Die Daten der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der registrierten externen Person (Zweitbetreuer*in)
     */
    @PostMapping("/register")
    @Operation(summary = "Registrierung einer externen Person",
            description = "Erstellt eine neue externe Person basierend auf den bereitgestellten Informationen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Erfolgreich registriert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExternalDTO.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"firstName\": \"Missy\",\n" +
                                    "  \"lastName\": \"Elliot\",\n" +
                                    "  \"email\": \"missy@test.com\",\n" +
                                    "  \"registrationDate\": \"2024-02-04\",\n" +
                                    "  \"role\": \"EXTERN\",\n" +
                                    "  \"company\": \"Big Company\",\n" +
                                    "  \"availabilityStart\": \"2023-12-12\",\n" +
                                    "  \"availabilityEnd\": \"2024-11-11\",\n" +
                                    "  \"description\": \"ich bin hoch motiviert\",\n" +
                                    "  \"specialFields\": [\n" +
                                    "    {\n" +
                                    "      \"name\": \"Cybersecurity\"\n" +
                                    "    },\n" +
                                    "  ],\n" +
                                    "  \"bachelorSubjects\": [\n" +
                                    "    {\n" +
                                    "      \"title\": \"Auswirkungen von Cyberangriffen auf Musiker_innen und Musikunternehmen\",\n" +
                                    "      \"date\": \"2024-02-04\",\n" +
                                    "      \"bdescription\": \"super Thema\"\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Ungültige Anfrage",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"Die E-Mail ist bereits vergeben!\"}"))),
    })
    public ResponseEntity<Object> register(@RequestBody ExternalRequest externalRequest) {
        return externalService.registration(externalRequest, false);
    }


    /**
     * Methode für das Laden aller externen Personen (Zweitbetreuer*innen)
     *
     * @return ResponseEntity mit den Daten aller externen Personen (Zweitbetreuer*innen)
     */
    @GetMapping("/load")
    @Operation(summary = "Lädt aller externen Personen",
            description = "Ruft eine Liste aller registrierten externen Personen ab.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExternalDTO.class))),
    })
    public ResponseEntity<List<ExternalDTO>> allExternals() {
        List<ExternalDTO> externalDTOS = externalService.getAllExternalsWithSubjects();
        return ResponseEntity.ok(externalDTOS);
    }

    /**
     * Methode für das Laden einer externen Person (Zweitbetreuer*in) per ID
     *
     * @param id Die ID der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der externen Person (Zweitbetreuer*in)
     */
    @GetMapping("/load/{id}")
    @Operation(summary = "Lädt einer externen Person",
            description = "Ruft eine externe Person basierend auf der bereitgestellten ID ab.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExternalDTO.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"firstName\": \"Missy\",\n" +
                                    "  \"lastName\": \"Elliot\",\n" +
                                    "  \"email\": \"missy@test.com\",\n" +
                                    "  \"registrationDate\": \"2024-02-04\",\n" +
                                    "  \"role\": \"EXTERN\",\n" +
                                    "  \"company\": \"Big Company\",\n" +
                                    "  \"availabilityStart\": \"2023-12-12\",\n" +
                                    "  \"availabilityEnd\": \"2024-11-11\",\n" +
                                    "  \"description\": \"ich bin hoch motiviert\",\n" +
                                    "  \"specialFields\": [\n" +
                                    "    {\n" +
                                    "      \"name\": \"Cybersecurity\"\n" +
                                    "    },\n" +
                                    "  ],\n" +
                                    "  \"bachelorSubjects\": [\n" +
                                    "    {\n" +
                                    "      \"title\": \"Auswirkungen von Cyberangriffen auf Musiker_innen und Musikunternehmen\",\n" +
                                    "      \"date\": \"2024-02-04\",\n" +
                                    "      \"bdescription\": \"super Thema\"\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"User not found\", \"userId\": 1}"))),
    })
    public ResponseEntity<ExternalDTO> getExternal(@PathVariable Long id) {
        ExternalDTO externalDTO = externalService.getExternal(id);
        return ResponseEntity.ok(externalDTO);
    }

    /**
     * Methode für das Laden einer externen Person (Zweitbetreuer*in) per E-Mail
     *
     * @param email Die E-Mail-Adresse der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der externen Person (Zweitbetreuer*in)
     */
    @GetMapping("/loadByEmail")
    @Operation(summary = "Lädt eine externen Person per Email",
            description = "Ruft eine externe Person basierend auf der bereitgestellten E-Mail ab.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExternalDTO.class))),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"Externe Person mit der E-Mail 'example@test.com' nicht gefunden.\"}"))),
    })
    public ResponseEntity<ExternalDTO> getExternalByEmail(@RequestParam String email) {
        ExternalDTO externalDTO = externalService.getExternalByEmail(email);
        return ResponseEntity.ok(externalDTO);
    }

    /**
     * Methode für das Löschen einer externen Person (Zweitbetreuer*in) per ID
     *
     * @param id Die ID der externen Person (Zweitbetreuer*in)
     * @param externalRequest Die Daten der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit einer Bestätigung
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Aktualisierung einer externen Person per ID",
            description = "Aktualisiert die Daten einer externen Person basierend auf der bereitgestellten ID " +
                    "und den bereitgestellten Informationen. Kann nur von eingeloggten User ausgeführt werden.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich aktualisiert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExternalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Anfrage",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"Die E-Mail ist bereits vergeben!\"}"))),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"User not found\", \"userId\": 1}"))),
    })
    public ResponseEntity<Object> updateExternal (@PathVariable Long id,
                                                  @RequestBody ExternalRequest externalRequest) {
        return externalService.updateExternal(id, externalRequest);
    }
    
}



