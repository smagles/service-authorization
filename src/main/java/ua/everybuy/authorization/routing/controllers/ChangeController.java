package ua.everybuy.authorization.routing.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Change a user's email address",
            description = "Allows an authenticated user to change their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email address changed successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"token\": \"generatedToken\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - wrong password or invalid token",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"...\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 404, \"error\": { \"message\": \"... not found!\" } }")
                    ) }),
            @ApiResponse(responseCode = "409", description = "Conflict, indicating the new email is already in use",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 409, \"error\": { \"message\": \"Email [email] is already taken\" } }")
                    ) })
    })
    @PutMapping("/change-email")
    public ResponseEntity<?> changeEmail(Principal principal,
                                         @RequestBody @Valid ChangeEmailRequest changeEmailRequest) {
        return service.changeEmail(principal.getName(), changeEmailRequest);
    }

    @Operation(summary = "Change a user's phone number",
            description = "Allows an authenticated user to change their phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone number changed successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"token\": \"generatedToken\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not found or wrong password or invalid token",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"...\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 404, \"error\": { \"message\": \"... not found!\" } }")
                    ) }),
            @ApiResponse(responseCode = "409", description = "Conflict, the new phone number is already in use by another user",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 409, \"error\": { \"message\": \"Phone number [newPhoneNumber] is already taken\" } }")
                    ) })
    })
    @PutMapping("/change-phone-number")
    public ResponseEntity<?> changePhoneNumber(Principal principal,
                                               @RequestBody @Valid ChangePhoneRequest changePhoneRequest) {
        return service.changePhoneNumber(principal.getName(), changePhoneRequest);
    }

    @Operation(summary = "Change a user's password",
            description = "Allows an authenticated user to change their password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"token\": \"generatedToken\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not found or wrong password or invalid token",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"...\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 404, \"error\": { \"message\": \"... not found!\" } }")
                    ) })
    })
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Principal principal,
                                            @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return service.changePassword(principal.getName(), changePasswordRequest);
    }
}
