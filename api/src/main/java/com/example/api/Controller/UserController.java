package com.example.api.Controller;

import com.example.api.Request.UserRequest;
import com.example.api.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Controller verbindung zum Client, hier werden die methoden ausgeführt
@RestController
// Pfad wird selbst festgelegt?
@RequestMapping(path ="/test/version1")
public class UserController {
    // mit Service verknüpfen/verbinden
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Mapping :Verbindung mit Client
    // Post : es wird in die Datenbank hingeschrieben
    // Get: es wird aus der Datenbank herausgeholt
    // zb Liste aller Studenten soll angezeigt werden auf Client
    // Pfad selbst angegeben - im Browser(Postman) dann localhost:8080/test/version1/register eingeben um Post zu testen/machen
    @PostMapping(path ="/register")
    //es wird angegeben, was man von Client Seite haben möchte - wir bekommen "Körper" vom Client
    // alle Attribute die im StudentRequest sind werden übergeben
    public String register(@RequestBody UserRequest userRequest){
        return userService.register(userRequest);
    }


    //@GetMapping
}
