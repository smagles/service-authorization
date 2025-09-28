package ua.everybuy.authorization.errorhandling;

import org.springframework.stereotype.Component;
import ua.everybuy.authorization.database.entity.AuthProvider;
import ua.everybuy.authorization.database.entity.User;

@Component
public class AuthProviderValidator {
    public void validateUserCanChangePassword(User user) {
        if (AuthProvider.GOOGLE.equals(user.getAuthProvider())) {
            throw new AuthProviderValidationException(
                    "Cannot change password for Google-authenticated account"
            );
        }
    }

    public void validateUserCanChangeEmail(User user) {
        if (AuthProvider.GOOGLE.equals(user.getAuthProvider())) {
            throw new AuthProviderValidationException(
                    "Cannot change email for Google-authenticated account"
            );
        }
    }


    public void validateUserCanLoginWithPassword(User user) {
        if (AuthProvider.GOOGLE.equals(user.getAuthProvider())) {
            throw new AuthProviderValidationException(
                    "This account is registered with Google. Please log in using Google authentication."
            );
        }
    }

}