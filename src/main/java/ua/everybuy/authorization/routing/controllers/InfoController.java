package ua.everybuy.authorization.routing.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.authorization.buisnesslogic.service.UserService;
import ua.everybuy.authorization.routing.dtos.GetPhoneRequest;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class InfoController {
    private final UserService userService;

    @GetMapping("/get-phone")
    public ResponseEntity<?> getPhone(@Valid GetPhoneRequest getPhoneRequest) {
        return userService.getPhone(getPhoneRequest.getUserId());
    }
}
