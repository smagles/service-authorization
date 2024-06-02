package ua.everybuy.authorization.routing.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPhoneRequest {
    @NotNull
    private Long userId;
}
