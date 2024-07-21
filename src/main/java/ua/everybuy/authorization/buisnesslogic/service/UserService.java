package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.SmsCode;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.database.repository.UserRepository;
import ua.everybuy.authorization.routing.dtos.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtServiceUtils jwtServiceUtils;
    private final PasswordEncoder passwordEncoder;
    private final SmsCodeService smsCodeService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)  //TODO phone?
                .orElseThrow(() -> new UsernameNotFoundException("Login " + username + " not found!"));
        return user;
    }

    public User createNewUser(User user) {
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

    public Optional<User> getOUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmail(String email) {//throws UsernameNotFoundException {
        return getOUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Login " + email + " not found!"));  //TODO
    }

    public ResponseEntity<?> changeEmail(String login, ChangeEmailRequest changeEmailRequest) {
        User user = getUserByEmail(login);

        if (!passwordEncoder.matches(changeEmailRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("Wrong password!")));
        }

        if (existsUserWithEmail(changeEmailRequest.getNewEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(HttpStatus.CONFLICT.value(),
                            new MessageResponse("Email " + changeEmailRequest.getNewEmail() + " is already taken")));
        }

        user.setEmail(changeEmailRequest.getNewEmail());
        userRepository.save(user);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new TokenResponse(jwtServiceUtils.generateToken(user)))
                .build());
    }

    public ResponseEntity<?> changePhoneNumber(String login, ChangePhoneRequest changePhoneRequest) {
        User user = getUserByEmail(login);

        if (!passwordEncoder.matches(changePhoneRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("Wrong password!")));
        }

        if (existsUserWithPhone(changePhoneRequest.getNewPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(HttpStatus.CONFLICT.value(),
                            new MessageResponse("Phone number " + changePhoneRequest.getNewPhoneNumber() + " is already taken")));
        }

        user.setPhoneNumber(changePhoneRequest.getNewPhoneNumber());
        userRepository.save(user);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new TokenResponse(jwtServiceUtils.generateToken(user)))
                .build());
    }

    public ResponseEntity<?> changePassword(String login, ChangePasswordRequest changePasswordRequest) {
        User user = getUserByEmail(login);

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("Wrong old password!")));
        }

        user.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new TokenResponse(jwtServiceUtils.generateToken(user)))
                .build());
    }

    public User setNewPassword(User user, String pass) {
        user.setPasswordHash(passwordEncoder.encode(pass));
        user.setPasswordResetAt(Timestamp.from(Instant.now()));
        return user;
    }

    public ResponseEntity<?> getPhone(long userId) {
        Optional<User> oUser = userRepository.findById(userId);

        if (oUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                            new MessageResponse("User with user id: " + userId + " not found!")));
        }

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new PhoneResponse(oUser.get().getPhoneNumber()))
                .build());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean existsUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsUserWithPhone(String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }

    public ResponseEntity<?> sendCodeToDel(String email) {
        User user = getUserByEmail(email);
        String code;

        code = smsCodeService.setSmsCode(user.getId());

        emailService.sendEmail(email, "Code to delete account", code);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new MessageResponse("Code send to your email!"))
                .build());
    }

    public ResponseEntity<?> deleteAccount(DeleteRequest deleteRequest, String email) {
        User user = getUserByEmail(email);
        Optional<SmsCode> oSmsCode;
        SmsCode smsCode;

        if (!passwordEncoder.matches(deleteRequest.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("Wrong password!")));
        }

        oSmsCode = smsCodeService.getOSmsCode(user.getId());  //TODO duplicate

        if (oSmsCode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                            new MessageResponse("Code not found!")));
        }

        smsCode = oSmsCode.get();

        if (!Objects.equals(smsCode.getCode(), deleteRequest.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            new MessageResponse("Wrong code!")));
        }

        if (!smsCodeService.isSmsCodeActual(smsCode)) {
            smsCodeService.removeSmsCode(smsCode);

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                            new MessageResponse("Your code has expired!")));
        }

        userRepository.delete(user);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new MessageResponse("Account removed!"))
                .build());
    }

    public List<Long> getUserIdsWithoutAction(Long actionId) {
        return userRepository.getUsersWithoutAction(actionId);
    }
}
