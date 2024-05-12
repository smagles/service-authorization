package ua.everybuy.authorization.routing.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.authorization.buisnesslogic.service.PasswordRecoveryService;
import ua.everybuy.authorization.routing.dtos.CodeRequest;
import ua.everybuy.authorization.routing.dtos.LoginRequest;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class PasswordRecoveryController {
    private final PasswordRecoveryService service;

    @PostMapping("/get-recovery-code")
    public ResponseEntity<?> getRecovery(@RequestBody @Valid LoginRequest loginRequest) {
        return service.sendCode(loginRequest.getLogin());
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestBody @Valid CodeRequest codeRequest) {
        return service.recoveryPassword(codeRequest);
    }
}
