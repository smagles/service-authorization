package ua.everybuy.authorization.routing.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    @NotNull
    @Email(message = "email should be valid",
            regexp = "^[a-z0-9_+&*-]+(?:\\.[a-z0-9_+&*-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,7}$")
    private String email;
    @NotNull
    @Pattern(message = "phone should be valid (9 numbers)", regexp = "^\\d{9}$")
    private String phone;
    @NotNull
    @Pattern(message = "password should be valid",
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#$%^&*()_\\-+={\\[}\\]|\\\\:\";'<>?,./])[a-zA-Z0-9~`!@#$%^&*()_\\-+={\\[}\\]|\\\\:\";'<>?,./]{8,25}$")
    private String password;
}
