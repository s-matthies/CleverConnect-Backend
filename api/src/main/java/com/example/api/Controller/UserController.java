package com.example.api.Controller;

import com.example.api.Entitys.User;
import com.example.api.Request.LoginRequest;
import com.example.api.Request.UserRequest;
import com.example.api.Security.auth.AuthenticationRequest;
import com.example.api.Security.auth.AuthenticationResponse;
import com.example.api.Security.auth.AuthenticationService;
import com.example.api.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller verbindung zum Client, hier werden die methoden ausgeführt

@RestController
// Pfad wird selbst festgelegt?
@RequestMapping(path ="user")
public class UserController {
    // mit Service verknüpfen/verbinden
    @Autowired
    private final UserService userService;
    private final AuthenticationService service;

    public UserController(UserService userService, AuthenticationService service) {
        this.userService = userService;
        this.service = service;
    }

    // Mapping :Verbindung mit Client
    // Post : es wird in die Datenbank hingeschrieben
    // Get: es wird aus der Datenbank herausgeholt
    // zb Liste aller Studenten soll angezeigt werden auf Client

    @PostMapping(path ="/register")
    //es wird angegeben, was man von Client Seite haben möchte - wir bekommen "Körper" vom Client
    // alle Attribute die im UserRequest sind, werden übergeben
    public ResponseEntity<Object> register(@RequestBody UserRequest userRequest){
        return userService.register(userRequest);
    }


    @PostMapping(path ="/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/login")
    public ResponseEntity<Object> signInUser(@RequestBody LoginRequest loginRequest) {
        return userService.signInUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    //alle User laden
    @GetMapping("/load")
    List<User> allUsers() {
        return userService.allUser();
    }

    //einzelne User mit ID laden
    @GetMapping("/load/{id}")
    User getUsers(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // Daten eines Users ändern
    /**
     * Aktualisiert einen Benutzer mit den bereitgestellten Informationen.
     * Diese Methode ist dafür verantwortlich, einen vorhandenen Benutzer mit der angegebenen
     * ID mithilfe der im Parameter {@code newUser} bereitgestellten Informationen zu aktualisieren.
     * Die Methode gibt ein ResponseEntity zurück, das die aktualisierten Benutzerinformationen im Erfolgsfall
     * oder eine JSON-Antwort mit einer Fehlermeldung im Fehlerfall enthält.
     *
     * @param id Die eindeutige Kennung des zu aktualisierenden Benutzers.
     * @param newUser Das User-Objekt, das die aktualisierten Informationen enthält.
     * @return ResponseEntity mit dem HTTP-Status 200 OK und den aktualisierten Benutzerinformationen im Erfolgsfall,
     *         oder ein ResponseEntity mit dem HTTP-Status 400 BAD REQUEST und einer Fehlermeldung im JSON-Format im Fehlerfall.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser (@PathVariable Long id, @RequestBody User newUser) {
        return userService.updateUser(id, newUser);

        /*
         * - ResponseEntity.ok(): Dies erstellt eine ResponseEntity-Instanz mit dem HTTP-Statuscode 200 OK.
         * Das bedeutet, dass die Anfrage erfolgreich war.
         * - .contentType(MediaType.APPLICATION_JSON): Hier wird der Content-Type der Antwort auf
         * MediaType.APPLICATION_JSON gesetzt. Dies teilt dem Client mit, dass der Inhalt der Antwort im
         * JSON-Format vorliegt.
         * - .body(userService.updateUser(id, newUser)): Hier wird der eigentliche Inhalt der Antwort festgelegt.
         * Der Inhalt wird von der Methode userService.updateUser(id, newUser) bereitgestellt.
         * Der Rückgabewert dieser Methode wird als Körper (body) der HTTP-Antwort gesetzt.
         */
    }

    // eine Userin löschen
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }


    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.signOut(request, response);
    }

}
