package com.example.api.Controller;

import com.example.api.DTO.SignInResponse;
import com.example.api.Entitys.User;
import com.example.api.Request.LoginRequest;
import com.example.api.Request.PasswordChangeRequest;
import com.example.api.Request.UserRequest;
import com.example.api.Security.auth.AuthenticationService;
import com.example.api.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Die Klasse UserController ist für die Verwaltung von Usern verantwortlich.
 * Sie bietet Methoden zum Registrieren, Einloggen, Ausloggen, Laden, Aktualisieren und Löschen von Benutzer*innen.
 * Sie bietet auch eine Methode zum Ändern des Passworts von Benutzer*innen.
 */
@RestController
@RequestMapping(path ="user")
public class UserController {


    @Autowired
    private final UserService userService;
    private final AuthenticationService service;

    /**
     * Konstruktor für die Klasse UserController.
     * Initialisiert den UserController mit einem UserService und einem AuthenticationService.
     *
     * @param userService Der Service für die Benutzer*innenverwaltung
     * @param service Der Service für die Authentifizierung
     */
    public UserController(UserService userService, AuthenticationService service) {
        this.userService = userService;
        this.service = service;
    }

    /**
     * Methode für die Registrierung eines Users.
     * Nimmt eine UserRequest entgegen und delegiert die Registrierung an den UserService.
     *
     * @param userRequest Die Anfrage mit den Daten des zu registrierenden Users
     * @return ResponseEntity mit den Daten des registrierten Users
     */
    @PostMapping(path ="/register")
    public ResponseEntity<Object> register(@RequestBody UserRequest userRequest){
        return userService.register(userRequest);
    }

    /** Methode für das Einloggen eines Users.
     * Nimmt eine LoginRequest entgegen und delegiert das Einloggen an den UserService.
     *
     * @param loginRequest Die Anfrage mit den Daten des zu einloggenden Users
     * @return ResponseEntity mit den Daten des eingeloggten Users
     */
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> signInUser(@RequestBody LoginRequest loginRequest) {
        return userService.signInUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    /**
     * Methode für das Laden aller User.
     * Delegiert das Laden aller User an den UserService.
     *
     * @return ResponseEntity mit den Daten aller User
     */
    @GetMapping("/load")
    List<User> allUsers() {
        return userService.allUser();
    }

    /**
     * Methode für das Laden eines Users.
     * Delegiert das Laden eines Users an den UserService.
     *
     * @param id Die ID des Users
     * @return ResponseEntity mit den Daten des Users
     */
    @GetMapping("/load/{id}")
    User getUsers(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * Aktualisiert einen User.
     * Nimmt eine ID und ein User-Objekt mit den aktualisierten Informationen entgegen
     * und delegiert das Aktualisieren an den UserService.
     *
     * @param id Die ID des Users
     * @param newUser Das User-Objekt, das die aktualisierten Informationen enthält
     * @return ResponseEntity mit den aktualisierten Benutzer*innen-Informationen
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser (@PathVariable Long id, @RequestBody User newUser) {
        return userService.updateUser(id, newUser);
    }

    /**
     * Löscht einen User.
     * Nimmt eine ID entgegen und delegiert das Löschen des Users an den UserService.
     *
     * @param id Die ID des zu löschenden Users
     * @return ResponseEntity mit den Daten des gelöschten Users
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * Methode für das Ausloggen eines Users.
     * Nimmt eine HttpServletRequest und eine HttpServletResponse entgegen und delegiert das Ausloggen an den UserService.
     *
     * @param request Die Anfrage mit den Daten des zu ausloggenden Users
     * @param response Die Antwort an den zu ausloggenden User
     * @return ResponseEntity mit den Daten des ausgeloggten Users
     */
    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.signOut(request, response);
    }

    /**
     * Methode für das Ändern des Passworts eines Users.
     * Nimmt ein JWT-Token und eine PasswordChangeRequest entgegen und delegiert das Ändern des Passworts an den UserService.
     *
     * @param token Das JWT-Token der Benutzer*in
     * @param request Die Anfrage mit den Daten des zu ändernden Users
     * @return ResponseEntity mit den Daten des geänderten Users
     */
    @PutMapping("/updatePassword/{id}")
    public ResponseEntity<Object> updatePassword(@RequestHeader("Authorization") String token,
                                                 @RequestBody PasswordChangeRequest request) {
        return userService.changePassword(token, request.getOldPassword(), request.getNewPassword());
    }


}
