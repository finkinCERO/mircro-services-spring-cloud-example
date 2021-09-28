package htw.kbe.authentification.controller;

import htw.kbe.authentification.model.JwtRequest;
import htw.kbe.authentification.model.JwtResponse;
import htw.kbe.authentification.service.AuthentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthentificationService authService;

    @Autowired
    public AuthController(final AuthentificationService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<JwtResponse> register(@RequestBody JwtRequest authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }
    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

}