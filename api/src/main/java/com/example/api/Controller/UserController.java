package com.example.api.Controller;

import com.example.api.Entitys.User;
import com.example.api.Request.LoginRequest;
import com.example.api.Request.UserRequest;
import com.example.api.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller verbindung zum Client, hier werden die methoden ausgeführt
@RestController
// Pfad wird selbst festgelegt?
@RequestMapping(path ="/user")
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
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest){
        return userService.register(userRequest);
    }

    //alle User laden
    @GetMapping("/load")
    List<User> allStudents() {
        return userService.allUser();
    }

    //einzelne User mit ID laden
    @GetMapping("/load/{id}")
    User getStudent(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // Daten eines Users ändern
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser (@PathVariable Long id, @RequestBody User newUser) {
        return userService.updateUser(id, newUser);
    }

    // eine Userin löschen
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> signInUser(@RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        return userService.signInUser(loginRequest.getEmail(), loginRequest.getPassword(), httpSession);
    }


    /*
    public String login (@RequestBody LoginRequest loginRequest) {
        boolean existsExternal = userService.login(loginRequest.getEmail(), loginRequest.getPassword()).isPresent();
        if(existsExternal) {
            return "Anmeldung war erfolgreich!";
        }
        throw new IllegalStateException("Userin wurde nicht gefunden!");
    }
     */



}
