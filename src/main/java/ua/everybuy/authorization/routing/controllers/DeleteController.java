package ua.everybuy.authorization.routing.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import ua.everybuy.authorization.buisnesslogic.service.UserService;
import ua.everybuy.authorization.routing.dtos.DeleteRequest;

import java.security.Principal;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class DeleteController {
    private final UserService service;

    @Operation(summary = "Send email with code",
            description = "Send email with code to delete account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Code send successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"message\": \"Code send to your email!\" } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - wrong password, invalid token or wrong code",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"...\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 404, \"error\": { \"message\": \"... [email] not found!\" } }")
                    ) })
    })
    @GetMapping("/get-code-to-del")
    public ResponseEntity<?> getCodeToDel(Principal principal) {
        return service.sendCodeToDel(principal.getName());
    }

    @Operation(summary = "Delete account",
            description = "Remove account with all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Removed successfully",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"message\": \"Account removed!\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - wrong password, invalid token or wrong code",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"...\" } }")
                    ) }),
            @ApiResponse(responseCode = "403", description = "Code has expired",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 403, \"error\": { \"message\": \"Your code has expired!\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 404, \"error\": { \"message\": \"... [email] not found!\" } }")
                    ) })
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestBody @Valid DeleteRequest deleteRequest, Principal principal) {
        return service.deleteAccount(deleteRequest, principal.getName());
    }
}
