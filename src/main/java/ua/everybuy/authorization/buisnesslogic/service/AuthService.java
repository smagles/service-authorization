package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.routing.dtos.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceUtils jwtServiceUtils;

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

        token = jwtServiceUtils.generateToken(userService.createNewUser(user));

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(token)
                .build());
    }

    public ResponseEntity<?> authorization(AuthRequest authRequest) {
        Optional<User> user = userService.getUserByLogin(authRequest.getLogin());
        String token;

        if (user.isEmpty() || !passwordEncoder.matches(authRequest.getPassword(), user.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("User " + authRequest.getLogin() + " not found or wrong password!"))); //TODO
        }

        token = jwtServiceUtils.generateToken(user.get());

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(token)
                .build());
    }
}
