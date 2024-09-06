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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller für die Admin-Funktionen
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ExternalService externalService;

    @Autowired
    public AdminController(ExternalService externalService) {
        this.externalService = externalService;
    }

    /**
     * Methode für das Anlegen einer externen Person (Zweitbetreuer*in)
     *
     * @param externalRequest Die Daten der externen Person
     * @return ResponseEntity mit den Daten der registrierten externen Person
     */
    @PostMapping("/createExternal")
    @Operation(summary = "Anlegen einer externen Person",
            description = "Erstellt eine neue externe Person basierend auf den bereitgestellten Informationen." +
                    "Das Anlegen einer externen Person ist nur für Admins möglich.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Erfolgreich erstellt",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExternalDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Anfrage",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"Die E-Mail ist bereits vergeben!\"}"))),
    })
    public ResponseEntity<Object> createExternal(@RequestBody ExternalRequest externalRequest) {
        return externalService.registration(externalRequest, true);
    }
}


