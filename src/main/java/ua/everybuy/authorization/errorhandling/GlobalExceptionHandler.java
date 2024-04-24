package ua.everybuy.authorization.errorhandling;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        StringBuilder errorText = new StringBuilder();
        for (FieldError error : result.getFieldErrors()) {
            errorText.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        new MessageResponse(errorText.toString().trim())));
    }

    @ExceptionHandler({ExpiredJwtException.class, ServletException.class, UsernameNotFoundException.class})
    @ResponseBody
    public ResponseEntity<?> handleJwtExceptions(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                        new MessageResponse(e.getMessage())));
    }
}
