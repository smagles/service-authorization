package ua.everybuy.authorization.routing.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Schema(description = "user's phone number or email")
    @NotNull
    @Pattern(message = "login must be email or phone (9 numbers)",
            regexp = "^[a-z0-9_+&*-]+(?:\\.[a-z0-9_+&*-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,7}$|^\\d{9}$")
    private String login;
}
