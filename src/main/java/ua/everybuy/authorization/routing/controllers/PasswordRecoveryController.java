package ua.everybuy.authorization.routing.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.authorization.buisnesslogic.service.PasswordRecoveryService;
import ua.everybuy.authorization.routing.dtos.RecoveryRequest;
import ua.everybuy.authorization.routing.dtos.LoginRequest;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class PasswordRecoveryController {
    private final PasswordRecoveryService service;

    @Operation(summary = "Send a recovery code",
            description = "Allows a user to request a recovery code via email for password recovery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recovery code sent to email successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"message\": \"An email has been sent to [email] with a recovery code." +
                                    " Please use this code to reset your password.\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"User [login] not found!\" } }")
                    ) })
    })
    @PostMapping("/get-recovery-code")
    public ResponseEntity<?> getRecovery(@RequestBody @Valid LoginRequest loginRequest) {
        return service.sendCode(loginRequest.getLogin());
    }

    @Operation(summary = "Recovery password using a recovery code",
            description = "Allows a user to recover their password using a valid recovery code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone number changed successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"token\": \"generatedToken\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "403", description = "Recovery code has expired",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 403, \"error\": { \"message\": \"Your password reset code has expired!\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "Recovery code or user not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 404, \"error\": { \"message\": \"... not found!\" } }"),
                            schema = @Schema
                    ) })
    })
    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestBody @Valid RecoveryRequest recoveryRequest) {
        return service.recoveryPassword(recoveryRequest);
    }
}
