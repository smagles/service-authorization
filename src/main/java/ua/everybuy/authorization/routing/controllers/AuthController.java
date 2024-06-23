package ua.everybuy.authorization.routing.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.everybuy.authorization.buisnesslogic.service.AuthService;
import ua.everybuy.authorization.routing.dtos.AuthRequest;
import ua.everybuy.authorization.routing.dtos.RegistrationRequest;

import java.security.Principal;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Registration new user",
            description = "Register a new user with the provided email, phone number, and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"token\": \"generatedToken\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "409", description = "Conflict - Email or phone already exists",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 409, \"error\": { \"message\": \"[email or phone] already in use;\" } }")
                    ) })
    })
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return authService.registration(registrationRequest);
    }

    @Operation(summary = "User login",
            description = "Endpoint for user authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"token\": \"generatedToken\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not found or wrong password",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"User [login] not found or wrong password!\" } }")
                    ) })
    })
    @PostMapping("/auth")
    public ResponseEntity<?> authorization(@RequestBody @Valid AuthRequest authRequest) {
        return authService.authorization(authRequest);
    }

    @Operation(summary = "Information about user",
            description = "Returns information about the user to whom the token belongs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The validation was successful",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"isValid\": true, \"userId\": 0," +
                                    " \"email\": \"user@mail.ua\", \"phoneNumber\": 123456789, \"roles\": [ \"USER\" ] } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - The provided token is invalid or has expired",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"...\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 404, \"error\": { \"message\": \"... [email] not found!\" } }")
                    ) })
    })
    @GetMapping("/validate")
    public ResponseEntity<?> validate(Principal principal) {
        return authService.validate(principal.getName());
    }
}
