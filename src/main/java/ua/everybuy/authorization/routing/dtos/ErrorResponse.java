package ua.everybuy.authorization.routing.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private MessageResponse error;
}
