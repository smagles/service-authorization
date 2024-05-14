package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.SmsCode;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.routing.dtos.*;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {
    private final SmsCodeService smsCodeService;
    private final UserService userService;
    private final JwtServiceUtils jwtServiceUtils;
    private final EmailService emailService;

    public ResponseEntity<?> sendCode(String login) {
        Optional<User> oUser =  userService.getOUserByEmail(login);
        User user;
        String code;

        if (oUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("User " + login + " not found!")));
        }

        user = oUser.get();
        code = smsCodeService.setSmsCode(user.getId());

        if (Objects.equals(login, user.getEmail())) {
            emailService.sendEmail(user.getEmail(), "Recovery code", code);

            return ResponseEntity.ok(StatusResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data("An email has been sent to " + user.getEmail()
                            + " with a recovery code. Please use this code to reset your password.")
                    .build());
        } else {
            // send sms //TODO
        }

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data("This endpoint is not available at this time.") //TODO
                .build());
    }

    public ResponseEntity<?> recoveryPassword(RecoveryRequest recoveryRequest) {
        Optional<User> oUser =  userService.getOUserByEmail(recoveryRequest.getLogin());
        User user;
        Optional<SmsCode> oSmsCode;
        SmsCode smsCode;

        if (oUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("User " + recoveryRequest.getLogin() + " not found!")));
        }

        user = oUser.get();
        oSmsCode = smsCodeService.getOSmsCode(user.getId());

        if (oSmsCode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                            new MessageResponse("Code not found!")));
        }

        smsCode = oSmsCode.get();

        if (!Objects.equals(smsCode.getCode(), recoveryRequest.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("Wrong code!")));
        }

        if (!smsCodeService.isSmsCodeActual(smsCode)) {
            smsCodeService.removeSmsCode(smsCode);

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                            new MessageResponse("Your password reset code has expired!")));
        }

        userService.setNewPassword(user, recoveryRequest.getNewPassword());
        userService.saveUser(user);
        smsCodeService.removeSmsCode(smsCode);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new TokenResponse(jwtServiceUtils.generateToken(user)))
                .build());
    }
}
