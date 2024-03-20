package ua.everybuy.authorization.routing.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    @NotNull
    //@Pattern(message = "email should be valid", regexp = "^(?:\\d{9}|[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7})$")
    private String login;
    @NotNull
    @Pattern(message = "password should be valid",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#$%^&*()_\\-+={\\[}\\]|\\\\:\";'<>?,./])[a-zA-Z0-9~`!@#$%^&*()_\\-+={\\[}\\]|\\\\:\";'<>?,./]{8,25}$")
    private String password;
}
