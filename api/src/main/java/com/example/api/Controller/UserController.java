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

/**
 * Controller für die Benutzer*innenverwaltung.
 * Dieser Controller stellt Endpunkte zur Verfügung, um Benutzer*innen zu registrieren, zu authentifizieren,
 * zu laden, zu aktualisieren, zu löschen und das Passwort zu ändern.
 */
@RestController
@RequestMapping(path ="user")
public class UserController {

    @Autowired
    private final UserService userService;
    private final AuthenticationService service;

    /**
     * Konstruktor der Klasse UserController.
     * @param userService Das UserService-Objekt für den Zugriff auf Benutzer*innendaten
     * @param service AuthenticationService für die Authentifizierung von Benutzer*innen
     */
    public UserController(UserService userService, AuthenticationService service) {
        this.userService = userService;
        this.service = service;
    }

    /**
     * Methode für die Registrierung einer Benutzer*in.
     * @param userRequest Die Anfrage mit den Daten der zu registrierenden Benutzer*in
     * @return ResponseEntity mit den Daten der registrierten Benutzer*in
     */
    @PostMapping(path ="/register")
    //es wird angegeben, was man von Client Seite haben möchte - wir bekommen "Körper" vom Client
    // alle Attribute die im UserRequest sind, werden übergeben
    public ResponseEntity<Object> register(@RequestBody UserRequest userRequest){
        return userService.register(userRequest);
    }

    /**
     * Methode für die Authentifizierung einer Benutzer*in.
     * @param request Die Anfrage mit den Daten der zu authentifizierenden Benutzer*in
     * @return ResponseEntity mit dem generierten JWT-Token
     */
    @PostMapping(path ="/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Methode für das Laden aller Benutzer*innen.
     * @return ResponseEntity mit den Daten aller Benutzer*innen
     */
    @PostMapping("/login")
    public ResponseEntity<Object> signInUser(@RequestBody LoginRequest loginRequest) {
        return userService.signInUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    /**
     * Methode für das Laden aller Benutzer*innen.
     * @return ResponseEntity mit den Daten aller Benutzer*innen
     */
    @GetMapping("/load")
    List<User> allUsers() {
        return userService.allUser();
    }

    /**
     * Lädt eine*n bestimmte*n Benutzer*in.
     * @param id Die ID der Benutzer*in
     * @return ResponseEntity mit den Daten der Benutzer*in
     */
    @GetMapping("/load/{id}")
    User getUsers(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * Aktualisiert einen Benutzer*in.
     * @param id Die ID des Benutzers*in
     * @param newUser Das User-Objekt, das die aktualisierten Informationen enthält
     * @return ResponseEntity mit den aktualisierten Benutzerinformationen
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser (@PathVariable Long id, @RequestBody User newUser) {
        return userService.updateUser(id, newUser);
    }

    /**
     * Löscht eine*n Benutzer*in.
     * @param id Die ID der Benutzerin
     * @return ResponseEntity mit den Daten der gelöschten Benutzer*in
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * Methode für das Ausloggen einer Benutzer*in.
     * @param request Die Anfrage mit den Daten der zu ausloggenden Benutzer*in
     * @param response Die Antwort mit den Daten der zu ausloggenden Benutzer*in
     * @return ResponseEntity mit den Daten der ausgeloggten Benutzer*in
     */
    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.signOut(request, response);
    }

    /**
     * Methode für das Ändern des Passworts einer Benutzer*in.
     * @param token Das JWT-Token der Benutzer*in
     * @param request Die Anfrage mit den Daten der zu ändernden Benutzer*in
     * @return ResponseEntity mit den Daten der geänderten Benutzer*in
     */
    @PutMapping("/updatePassword/{id}")
    public ResponseEntity<Object> updatePassword(@RequestHeader("Authorization") String token,
                                                 @RequestBody PasswordChangeRequest request) {
        return userService.changePassword(token, request.getOldPassword(), request.getNewPassword());
    }


}
