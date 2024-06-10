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
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {
    private final SmsCodeService smsCodeService;
    private final UserService userService;
    private final JwtServiceUtils jwtServiceUtils;
    private final EmailService emailService;
    private static final short ASCII_CHARS_START = 33;
    private static final short ASCII_CHARS_COUNT = 93;
    private static final short ASCII_NUMBERS_START = 48;
    private static final short ASCII_NUMBERS_COUNT = 10;
    private static final short ASCII_UPPERCASE_START = 65;
    private static final short ASCII_LOWERCASE_START = 97;
    private static final short ASCII_LETTERS_COUNT = 25;
    private static final String ASCII_SPECIALS = "~`!@#$%^&*()_\\-+={\\[}\\]|\\\\:\";'<>?,./";


    public ResponseEntity<?> sendCode(String login) {
        Optional<User> oUser =  userService.getOUserByEmail(login);
        User user;
        String code;

        if (oUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                            new MessageResponse("User " + login + " not found!")));
        }

        user = oUser.get();
        code = smsCodeService.setSmsCode(user.getId());

        if (Objects.equals(login, user.getEmail())) {
            emailService.sendEmail(user.getEmail(), "Запит на відновлення паролю", String.format("""
                <!DOCTYPE html>
                <html lang="uk">
                <head>
                    <meta charset="UTF-8">
                    <title>Код</title>
                    <style>
                        p, h1 {
                            font-family: Inter;
                        }
                
                        body {
                            background-color: #F6F6F6;
                        }
                
                        .logo-container {
                            display: flex;
                            margin: 0 auto;
                            text-align: center;
                            justify-content: center;
                            margin: 40px;
                        }
                
                        .logo {
                            background-image: url('data:image/png;base64,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');
                            background-size: cover;
                            background-position: center;
                            height: 77px;
                            width: 104px;
                        }
                
                        .code-container {
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            margin: 0 auto;
                            margin: 40px;
                        }
                
                        .code-cell {
                            width: 50px;
                            height: 50px;
                            border: 1px solid #9D9D9D;
                            padding: 5px;
                            margin: 7.5px;
                            text-align: center;
                            border-radius: 6px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            background-color: #FFFFFF;
                        }
                
                        .code {
                            font-family: Inter;
                            font-size: 35px;
                        }
                    </style>
                </head>
                <body>
                    <div class="logo-container">
                        <div class="logo"></div>
                    </div>
                    <h1>Запит на відновлення паролю</h1>
                    <p>Вітаємо, Вікторіє!</p>
                    <p>Ви отримали це повідомлення, тому що на ваш обліковий запис було надіслано запит на зміну пароля.</p>
                    <p>Ваш код на відновлення паролю:</p>
                    <div class="code-container">
                        <div class="code-cell">
                            <code class="code">%s</code>
                        </div>
                        <div class="code-cell">
                            <code class="code">%s</code>
                        </div>
                        <div class="code-cell">
                            <code class="code">%s</code>
                        </div>
                        <div class="code-cell">
                            <code class="code">%s</code>
                        </div>
                    </div>
                    <p>Якщо ви не запитували зміну пароля, будь ласка, ігноруйте це повідомлення або зв'яжіться з нашою службою підтримки.</p>
                    <p>З повагою,<br>Команда EveryBuy.</p>
                </body>
                </html>
                """, code.charAt(0), code.charAt(1), code.charAt(2), code.charAt(3)));

            return ResponseEntity.ok(StatusResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(new MessageResponse("An email has been sent to " + user.getEmail()
                            + " with a recovery code. Please use this code to reset your password."))
                    .build());
        } //TODO send sms

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ErrorResponse(HttpStatus.NOT_IMPLEMENTED.value(),
                        new MessageResponse("Password recovery by phone number is not implemented.")));
    }

    public ResponseEntity<?> recoveryPassword(RecoveryRequest recoveryRequest) {
        Optional<User> oUser =  userService.getOUserByEmail(recoveryRequest.getLogin());
        User user;
        Optional<SmsCode> oSmsCode;
        SmsCode smsCode;
        String newPass;

        if (oUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
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

        newPass = genePass();

        emailService.sendEmail(user.getEmail(), "New password", newPass);

        userService.setNewPassword(user, newPass);
        userService.saveUser(user);
        smsCodeService.removeSmsCode(smsCode);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new TokenResponse(jwtServiceUtils.generateToken(user)))
                .build());
    }

    private String genePass() {
        StringBuilder pass = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            pass.append((char) (random.nextInt(ASCII_CHARS_COUNT) + ASCII_CHARS_START));
        }
        pass.insert(random.nextInt(pass.length()),
                (char) (random.nextInt(ASCII_NUMBERS_COUNT) + ASCII_NUMBERS_START));
        pass.insert(random.nextInt(pass.length()),
                (char) (random.nextInt(ASCII_LETTERS_COUNT) + ASCII_UPPERCASE_START));
        pass.insert(random.nextInt(pass.length()),
                (char) (random.nextInt(ASCII_LETTERS_COUNT) + ASCII_LOWERCASE_START));
        pass.insert(random.nextInt(pass.length()), ASCII_SPECIALS.charAt(random.nextInt(ASCII_SPECIALS.length())));

        return pass.toString();
    }
}
