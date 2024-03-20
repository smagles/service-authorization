package ua.everybuy.authorization.routing.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder  //TODO
public class StatusResponse {
    private int status;
    private Object data;
}
