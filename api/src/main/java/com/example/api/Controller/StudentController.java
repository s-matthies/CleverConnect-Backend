package com.example.api.Controller;

import com.example.api.Request.StudentRequest;
import com.example.api.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Controller verbindung zum Client, hier werden die methoden ausgeführt
@RestController
// Pfad wird selbst festgelegt?
@RequestMapping(path ="/test/version1")
public class StudentController {
    // mit Service verknüpfen/verbinden
    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Mapping :Verbindung mit Client
    // Post : es wird in die Datenbank hingeschrieben
    // Get: es wird aus der Datenbank herausgeholt
    // zb Liste aller Studenten soll angezeigt werden auf Client
    // Pfad selbst angegeben - im Browser(Postman) dann localhost:8080/test/version1/register eingeben um Post zu testen/machen
    @PostMapping(path ="/register")
    //es wird angegeben, was man von Client Seite haben möchte - wir bekommen "Körper" vom Client
    // alle Attribute die im StudentRequest sind werden übergeben
    public String register(@RequestBody StudentRequest studentRequest){
        return studentService.register(studentRequest);
    }


    //@GetMapping
}
