package ua.everybuy.authorization.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.everybuy.authorization.buisnesslogic.service.JwtServiceUtils;
import ua.everybuy.authorization.database.entity.CustomOAuth2User;
import ua.everybuy.authorization.database.entity.User;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String SUCCESS_REDIRECT_URL = "/auth/google/success";
    private static final String TOKEN_PARAMETER = "token";
    private final JwtServiceUtils jwtServiceUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();

        User user = customUser.getUser();
        String jwtToken = jwtServiceUtils.generateToken(user);

        String redirectUrl = SUCCESS_REDIRECT_URL
                + "?" + TOKEN_PARAMETER
                + "=" + jwtToken;
        response.sendRedirect(redirectUrl);
    }
}
