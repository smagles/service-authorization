package ua.everybuy.authorization.routing.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.everybuy.authorization.routing.dtos.AuthRequest;
import ua.everybuy.authorization.routing.dtos.RegistrationRequest;
import ua.everybuy.authorization.buisnesslogic.service.AuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return authService.registration(registrationRequest);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authorization(@RequestBody @Valid AuthRequest authRequest) {
        return authService.authorization(authRequest);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate() {
        return ResponseEntity.ok(true);
    }
}
