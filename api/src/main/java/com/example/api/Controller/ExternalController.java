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

    // Userin (Externe) in die DB speichern
    /*
    - @RequestBody: was wir von der User-Eingabe bekommen
    - wir wollen die Attribute aus dem externRequest haben
    - vom Client bekommen wir alle Attribute vom ExternRequest und erstellen damit die neue Userin
    */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody ExternalRequest externalRequest) {
        return externalService.registration(externalRequest);
    }

    // alle Externen mit jeweiligen BachelorSubjects laden
    @GetMapping("/load")
    public ResponseEntity<List<ExternalDTO>> allExternals() {
        List<ExternalDTO> externalDTOS = externalService.getAllExternalsWithSubjects();
        return ResponseEntity.ok(externalDTOS);
    }

    /*
    //Alle Externen laden ohne BachelorSubjects
    @GetMapping("/load")
    List<External> allExtern() {
        return externalService.allExternal();
    }

     */

    //einzelne Externe mit ID laden
    @GetMapping("/load/{id}")
    External getExternal(@PathVariable Long id) {
        return externalService.getExternal(id);
    }

    // Daten einer Externen aktualisieren
    @PutMapping("/update/{id}")
    public ResponseEntity<External> updateExternal (@PathVariable Long id, @RequestBody External newUser) {
        List<BachelorSubject> updatedBachelorSubjects = newUser.getBachelorSubjects();
        return externalService.updateExternal(id, newUser);
    }

    // External mit BachelorSubjects laden
    @GetMapping("/{externalId}/bachelorSubjects")
    public ResponseEntity<ExternalDTO> getExternalWithSubjects(@PathVariable Long externalId) {
        External external = externalService.getExternal(externalId);

        List<BachelorSubject> bachelorSubjects = externalService.getBachelorSubjectsForExternal(externalId);
        ExternalDTO externalWithSubjectsDTO = new ExternalDTO(external, bachelorSubjects);

        return ResponseEntity.ok(externalWithSubjectsDTO);
    }

}



