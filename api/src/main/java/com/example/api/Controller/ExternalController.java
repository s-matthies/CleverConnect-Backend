package com.example.api.Controller;

import com.example.api.DTO.ExternalDTO;
import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.External;
import com.example.api.Request.ExternalRequest;
import com.example.api.Service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/external")
public class ExternalController {
    @Autowired
    //mit dem Service verbinden
    private final ExternalService externalService;

    public ExternalController(ExternalService externalService) {
        this.externalService = externalService;
    }


    /**
     * Methode für die Registrierung einer externen Person (Zweitbtreuer*in)
     * @param externalRequest Die Anfrage mit den Daten der zu registrierenden Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der registrierten Person (Zweitbetreuer*in)
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody ExternalRequest externalRequest) {
        return externalService.registration(externalRequest);
    }

    /**
     * Methode für das Laden aller externen Personen (Zweitbetreuer*innen)
     * @return ResponseEntity mit den Daten aller externen Personen (Zweitbetreuer*innen)
     */
    @GetMapping("/load")
    public ResponseEntity<List<ExternalDTO>> allExternals() {
        List<ExternalDTO> externalDTOS = externalService.getAllExternalsWithSubjects();
        return ResponseEntity.ok(externalDTOS);
    }

    /**
     * Methode für das Laden einer externen Person (Zweitbetreuer*in)
     * @param id Die ID der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der externen Person (Zweitbetreuer*in)
     */
    @GetMapping("/load/{id}")
    public ResponseEntity<ExternalDTO> getExternal(@PathVariable Long id) {
        ExternalDTO externalDTO = externalService.getExternal(id);
        return ResponseEntity.ok(externalDTO);
    }

    /**
     * Methode für das Löschen einer externen Person (Zweitbetreuer*in)
     * @param id Die ID der externen Person (Zweitbetreuer*in)
     * @return ResponseEntity mit den Daten der gelöschten externen Person (Zweitbetreuer*in)
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateExternal (@PathVariable Long id,
                                                  @RequestBody ExternalRequest externalRequest) {
        return externalService.updateExternal(id, externalRequest);
    }
    
}



