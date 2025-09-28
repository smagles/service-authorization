package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.errorhandling.AuthProviderValidator;
import ua.everybuy.authorization.routing.dtos.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceUtils jwtServiceUtils;
    private final SenderToUserService senderToUserService;
    private final AuthProviderValidator authProviderValidator;

    public ResponseEntity<?> registration(RegistrationRequest request) {
        User user = new User();
        String token;
        String errorMessage = "";

        if (userService.existsUserWithEmail(request.getEmail())) {
            errorMessage = "Email " + request.getEmail() + " already in use; ";
        }
        if (userService.existsUserWithPhone(request.getPhone())) {
            errorMessage += "Phone " + request.getPhone() + " already in use;";
        }
        if (!errorMessage.equals("")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(HttpStatus.CONFLICT.value(),
                            new MessageResponse(errorMessage.trim())));
        }

        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        user = userService.createNewUser(user);

        senderToUserService.sendNewUserCreate(user.getId());

        token = jwtServiceUtils.generateToken(user);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new TokenResponse(token))
                .build());
    }

    public ResponseEntity<?> authorization(AuthRequest authRequest) {
        Optional<User> user = userService.getOUserByLogin(authRequest.getLogin());
        String token;

        user.ifPresent(authProviderValidator::validateUserCanLoginWithPassword);

        if (user.isEmpty() || !passwordEncoder.matches(authRequest.getPassword(), user.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("User " + authRequest.getLogin()
                                    + " not found or wrong password!"))); //TODO
        }

        token = jwtServiceUtils.generateToken(user.get());

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new TokenResponse(token))
                .build());
    }

    public ResponseEntity<?> validate(String login) {
        User user = userService.getUserByEmail(login);
        List<String> rolesList = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new ValidResponse(!user.isBlock(), user.getId(), user.getUsername(), user.getPhoneNumber(), rolesList))
                .build());
    }
}
