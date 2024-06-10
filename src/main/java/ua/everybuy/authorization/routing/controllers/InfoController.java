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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.authorization.buisnesslogic.service.UserService;
import ua.everybuy.authorization.routing.dtos.GetPhoneRequest;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class InfoController {
    private final UserService userService;

    @Operation(summary = "Get phone",
            description = "Return user phone by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 200, \"data\": { \"token\": \"generatedToken\" } }")
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 400, \"error\": { \"message\": \"[field]: should be valid; (and|or) [field]: must not be null;\" } }")
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - The provided token is invalid or has expired",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 401, \"error\": { \"message\": \"...\" } }")
                    ) }),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = { @Content(mediaType = "application/json",
                            examples = @ExampleObject("{ \"status\": 409, \"error\": { \"message\": \"User with user id: [userId] not found!\" } }")
                    ) })
    })
    @GetMapping("/get-phone")
    public ResponseEntity<?> getPhone(@Valid GetPhoneRequest getPhoneRequest) {
        return userService.getPhone(getPhoneRequest.getUserId());
    }
}
