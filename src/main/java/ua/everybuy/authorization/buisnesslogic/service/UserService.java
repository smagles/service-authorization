package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.database.repository.UserRepository;
import ua.everybuy.authorization.routing.dtos.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtServiceUtils jwtServiceUtils;
    private final PasswordEncoder passwordEncoder;

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
                            new MessageResponse("Email " + changeEmailRequest.getNewEmail() + " already taken")));
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
                            new MessageResponse("Phone number " + changePhoneRequest.getNewPhoneNumber() + " already taken")));
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
}
