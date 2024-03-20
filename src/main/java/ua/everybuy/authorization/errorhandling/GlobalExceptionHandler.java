package ua.everybuy.authorization.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.everybuy.authorization.routing.dtos.ErrorResponse;
import ua.everybuy.authorization.routing.dtos.MessageResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError error : result.getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
        String errorText = "";
        for (FieldError error : result.getFieldErrors()) {
            errorText += error.getField() + ": " + error.getDefaultMessage() + "; ";
        }
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), new MessageResponse(errorText.trim())));
    }
}
