package ua.everybuy.authorization.routing.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.authorization.buisnesslogic.service.UserService;
import ua.everybuy.authorization.routing.dtos.ChangeEmailRequest;
import ua.everybuy.authorization.routing.dtos.ChangePasswordRequest;
import ua.everybuy.authorization.routing.dtos.ChangePhoneRequest;

import java.security.Principal;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class ChangeController {
    private final UserService service;

    @PutMapping("/change-email")
    public ResponseEntity<?> changeEmail(Principal principal,
                                         @RequestBody @Valid ChangeEmailRequest changeEmailRequest) {
        return service.changeEmail(principal.getName(), changeEmailRequest);
    }

    @PutMapping("/change-phone-number")
    public ResponseEntity<?> changePhoneNumber(Principal principal,
                                               @RequestBody @Valid ChangePhoneRequest changePhoneRequest) {
        return service.changePhoneNumber(principal.getName(), changePhoneRequest);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Principal principal,
                                            @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return service.changePassword(principal.getName(), changePasswordRequest);
    }
}
