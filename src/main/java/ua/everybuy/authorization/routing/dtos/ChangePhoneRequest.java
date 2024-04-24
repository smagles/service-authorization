package ua.everybuy.authorization.routing.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePhoneRequest {
    @NotNull
    private String password;
    @NotNull
    @Pattern(message = "phone should be valid (9 numbers)", regexp = "^\\d{9}$")
    private String newPhoneNumber;
}
