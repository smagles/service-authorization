package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.AuthProvider;
import ua.everybuy.authorization.database.entity.CustomOAuth2User;
import ua.everybuy.authorization.database.entity.User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;
    private final SenderToUserService senderToUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_email"),
                    "Google account does not provide an email."
            );
        }

        String userName = oauth2User.getAttribute("name");
        String userPhotoUrl = oauth2User.getAttribute("picture");

        User user = userService.getOUserByEmail(email)
                .map(existing -> validateAuthProvider(existing, email))
                .orElseGet(() -> registerNewUser(email, userName, userPhotoUrl));

        return new CustomOAuth2User(oauth2User, user);
    }

    private User validateAuthProvider(User user, String email) {
        if (AuthProvider.LOCAL.equals(user.getAuthProvider())) {
            String errorMessage = "This email" + email
                    + " is already registered. Please login with your email and password.";
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("email_already_used", errorMessage, null),
                    errorMessage
            );
        }
        return user;
    }

    private User registerNewUser(String email, String userName, String userPhotoUrl) {
        User user = new User();
        user.setEmail(email);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setPhoneNumber("");
        user.setPasswordHash(UUID.randomUUID().toString().substring(0, 16));
        user = userService.createNewUser(user);

        senderToUserService.sendNewUserFromGoogle(user.getId(), userName, userPhotoUrl);
        return user;
    }
}
