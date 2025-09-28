package ua.everybuy.authorization.routing.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.authorization.routing.dtos.ErrorResponse;
import ua.everybuy.authorization.routing.dtos.MessageResponse;
import ua.everybuy.authorization.routing.dtos.TokenResponse;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class GoogleAuthController {
    @GetMapping("/google/success")
    public TokenResponse authSuccess(@RequestParam String token) {
        return new TokenResponse(token);
    }

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/google/error")
    public ErrorResponse googleLoginError(@RequestParam String message) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                new MessageResponse(message));
    }
}
