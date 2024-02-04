package com.example.api.Controller;

import com.example.api.DTO.SignInResponse;
import com.example.api.Entitys.User;
import com.example.api.Request.LoginRequest;
import com.example.api.Request.PasswordChangeRequest;
import com.example.api.Request.UserRequest;
import com.example.api.Security.auth.AuthenticationService;
import com.example.api.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
    @Operation(summary = "Registriert eine Studierende*",
            description = "Erstellt eine neuen User (Studierende*) basierend auf den bereitgestellten Informationen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Erfolgreich registriert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "\"id\": 1,\n" +
                                    "\"firstName\": \"Nicki\",\n" +
                                    "\"lastName\": \"Minaj\",\n" +
                                    "\"email\": \"nicki.minaj@test.com\",\n" +
                                    "\"password\": \"$2a$10$FqKFfbM7/caXdXjoVjbtNuGHXHIU4GW4pznpbK3e1MIcN5N0UjI/i\",\n" +
                                    "\"registrationDate\": \"2024-01-17\",\n" +
                                    "\"role\": \"STUDENT\",\n" +
                                    "\"enabled\": true,\n" +
                                    "\"username\": \"nicki.minaj@test.com\",\n" +
                                    "\"authorities\": [\n" +
                                    "{\n" +
                                    "\"authority\": \"STUDENT\"\n" +
                                    "}\n" +
                                    "],\n" +
                                    "\"accountNonExpired\": true,\n" +
                                    "\"credentialsNonExpired\": true,\n" +
                                    "\"accountNonLocked\": true\n" +
                                    "}"))),

            @ApiResponse(responseCode = "400", description = "Ungültige Anfrage",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"E-Mail Adresse ist bereits vergeben!\"}")))
    })
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
    @Operation(summary = "Meldet einen User an",
            description = "Authentifiziert einen User basierend auf den bereitgestellten Informationen ein.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich eingeloggt",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignInResponse.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "\"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuaWNraS5taW5hampAdGVzdC5jb20\",\n" +
                                    "\"role\": \"STUDENT\",\n" +
                                    "\"id\": 1\n" +
                                    "}"))),
            @ApiResponse(responseCode = "401", description = "Nicht autorisiert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"Login war nicht erfolgreich! Email oder Passwort nicht korrekt!\"}")))
    })
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
    @Operation(summary = "Lädt alle Studierenden (und Admins)",
            description = "Ruft eine Liste aller registrierten Studierenden (und Admins) ab.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "[\n" +
                                    "  {\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"firstName\": \"John\",\n" +
                                    "    \"lastName\": \"Doe\",\n" +
                                    "    \"email\": \"john.doe@example.com\",\n" +
                                    "    \"password\": \"$2a$10$FqKFfbM7/caXdXjoVjbtNuGHXHIU4GW4pznpbK3e1MIcN5N0UjI/i\",\n" +
                                    "    \"registrationDate\": \"2024-02-04\",\n" +
                                    "    \"role\": \"STUDENT\",\n" +
                                    "    \"enabled\": true,\n" +
                                    "    \"username\": \"johndoe\",\n" +
                                    "    \"authorities\": [\n" +
                                    "      {\n" +
                                    "        \"authority\": \"STUDENT\"\n" +
                                    "      }\n" +
                                    "    ],\n" +
                                    "    \"credentialsNonExpired\": true,\n" +
                                    "    \"accountNonExpired\": true,\n" +
                                    "    \"accountNonLocked\": true\n" +
                                    "  }\n" +
                                    "]"))),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
    })
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
    @Operation(summary = "Lädt eine Studierende (oder Admin)",
            description = "Ruft einen User basierend auf der bereitgestellten ID ab.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich geladen",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"firstName\": \"John\",\n" +
                                    "  \"lastName\": \"Doe\",\n" +
                                    "  \"email\": \"john.doe@example.com\",\n" +
                                    "  \"password\": \"$2a$10$FqKFfbM7/caXdXjoVjbtNuGHXHIU4GW4pznpbK3e1MIcN5N0UjI/i\",\n" +
                                    "  \"registrationDate\": \"2024-02-04\",\n" +
                                    "  \"role\": \"STUDENT\",\n" +
                                    "  \"enabled\": true,\n" +
                                    "  \"username\": \"johndoe\",\n" +
                                    "  \"authorities\": [\n" +
                                    "    {\n" +
                                    "      \"authority\": \"STUDENT\"\n" +
                                    "    }\n" +
                                    "  ],\n" +
                                    "  \"credentialsNonExpired\": true,\n" +
                                    "  \"accountNonExpired\": true,\n" +
                                    "  \"accountNonLocked\": true\n" +
                                    "}"
                            ))),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"User not found\", \"userId\": 1"))),
    })
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
    @Operation(summary = "Aktualisiert eine Studierende* (oder Admin)",
            description = "Aktualisiert einen User basierend auf der bereitgestellten ID und den aktualisierten Informationen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich aktualisiert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"firstName\": \"John\",\n" +
                                    "  \"lastName\": \"Doe\",\n" +
                                    "  \"email\": \"john.doe@example.com\",\n" +
                                    "  \"password\": \"$2a$10$FqKFfbM7/caXdXjoVjbtNuGHXHIU4GW4pznpbK3e1MIcN5N0UjI/i\",\n" +
                                    "  \"registrationDate\": \"2024-02-04\",\n" +
                                    "  \"role\": \"STUDENT\",\n" +
                                    "  \"enabled\": true,\n" +
                                    "  \"username\": \"johndoe\",\n" +
                                    "  \"authorities\": [\n" +
                                    "    {\n" +
                                    "      \"authority\": \"STUDENT\"\n" +
                                    "    }\n" +
                                    "  ],\n" +
                                    "  \"credentialsNonExpired\": true,\n" +
                                    "  \"accountNonExpired\": true,\n" +
                                    "  \"accountNonLocked\": true\n" +
                                    "}"
                            ))),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"User not found\", \"userId\": 1}"))),
    })
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
    @Operation(summary = "Löscht einen User",
            description = "Löscht einenUser basierend auf der bereitgestellten ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich gelöscht",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"User mit der ID 1 erfolgreich gelöscht!\"}"))),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"error\": \"User mit der ID 1 wurde nicht gefunden.\"}"))),
    })
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
    @Operation(summary = "Meldet einen User ab",
            description = "Beendet die aktuelle Sitzung und meldet die*den Benutzer*in ab.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Erfolgreich abgemeldet",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"Logout erfolgreich\"}"))),
            @ApiResponse(responseCode = "401", description = "Nicht autorisiert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"Es ist kein Benutzer eingeloggt.\"}"))),
    })
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
    @PutMapping("/updatePassword")
    @Operation(summary = "Ändert das Passwort eines Users",
            description = "Ändert das Passwort eines Users basierend auf der bereitgestellten ID und den neuen Informationen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passwort erfolgreich geändert",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"Passwort erfolgreich geändert\"}"))),
            @ApiResponse(responseCode = "400", description = "Ungültige Anfrage",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"Das alte Passwort ist nicht korrekt!\"}"))),
            @ApiResponse(responseCode = "404", description = "Nicht gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(value = "{\"message\": \"User mit der ID 1 nicht gefunden.\"}"))),
    })
    public ResponseEntity<Object> updatePassword(@RequestHeader("Authorization") String token,
                                                 @RequestBody PasswordChangeRequest request) {
        return userService.changePassword(token, request.getOldPassword(), request.getNewPassword());
    }


    @GetMapping("/swagger")
    public RedirectView redirectToSwagger() {
        return new RedirectView("http://localhost:3000/swagger-ui/index.html#/");
    }


}
