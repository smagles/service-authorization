package ua.everybuy.authorization.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final String ERROR_REDIRECT_URL = "/auth/google/error?message=";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errorMessage = "OAuth2 Authentication failed";

        if (exception instanceof OAuth2AuthenticationException oauth2Ex) {
            OAuth2Error error = oauth2Ex.getError();

            if (error.getDescription() != null && !error.getDescription().isBlank()) {
                errorMessage = error.getDescription();
            } else if (error.getErrorCode() != null) {
                errorMessage = "OAuth2 error: " + error.getErrorCode();
            }
        } else if (exception.getMessage() != null) {
            errorMessage = exception.getMessage();
        }

        String redirectUrl = ERROR_REDIRECT_URL + URLEncoder
                .encode(errorMessage, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);
    }
}
