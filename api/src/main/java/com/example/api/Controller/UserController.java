package com.example.api.Controller;

import com.example.api.Entitys.User;
import com.example.api.Request.LoginRequest;
import com.example.api.Request.PasswordChangeRequest;
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

    @PutMapping("/updatePassword/{id}")
    public ResponseEntity<Object> updatePassword(@RequestHeader("Authorization") String token,
                                                 @RequestBody PasswordChangeRequest request) {
        return userService.changePassword(token, request.getOldPassword(), request.getNewPassword());
    }


}
