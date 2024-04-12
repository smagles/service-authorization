package ua.everybuy.authorization.routing.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidResponse {
    private Boolean isValid;
    private long userId;
    private String email;
    private String phoneNumber;
    private List<String> roles;
}
