package ua.everybuy.authorization.errorhandling;

public class AuthProviderValidationException extends RuntimeException {

    public AuthProviderValidationException(String message) {
        super(message);
    }
}
