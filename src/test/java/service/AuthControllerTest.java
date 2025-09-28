package service;

import static config.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.everybuy.authorization.buisnesslogic.service.AuthService;
import ua.everybuy.authorization.buisnesslogic.service.JwtServiceUtils;
import ua.everybuy.authorization.buisnesslogic.service.SenderToUserService;
import ua.everybuy.authorization.buisnesslogic.service.UserService;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.errorhandling.AuthProviderValidator;
import ua.everybuy.authorization.routing.dtos.*;

import java.util.Optional;

public class AuthControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtServiceUtils jwtServiceUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SenderToUserService senderToUserService;
    @Mock
    private AuthProviderValidator authProviderValidator;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrationEmailAlreadyInUse() {
        RegistrationRequest request = getRegistrationRequestSupplier.get();

        when(userService.existsUserWithEmail(EMAIL)).thenReturn(true);
        when(userService.existsUserWithPhone(PHONE)).thenReturn(false);

        ResponseEntity<?> response = authService.registration(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email " + EMAIL + " already in use;", ((ErrorResponse) response.getBody()).getError().getMessage());
    }

    @Test
    public void testRegistrationPhoneAlreadyInUse() {
        RegistrationRequest request = getRegistrationRequestSupplier.get();

        when(userService.existsUserWithEmail(EMAIL)).thenReturn(false);
        when(userService.existsUserWithPhone(PHONE)).thenReturn(true);

        ResponseEntity<?> response = authService.registration(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Phone " + PHONE + " already in use;", ((ErrorResponse) response.getBody()).getError().getMessage());
    }

    @Test
    public void testRegistrationSuccess() {
        RegistrationRequest request = getRegistrationRequestSupplier.get();
        User newUser = getUserSupplier.get();

        when(userService.existsUserWithEmail(EMAIL)).thenReturn(false);
        when(userService.existsUserWithPhone(PHONE)).thenReturn(false);
        when(passwordEncoder.encode("P@ssw0rd")).thenReturn("encodedPassword");
        when(userService.createNewUser(any(User.class))).thenReturn(newUser);
        when(jwtServiceUtils.generateToken(newUser)).thenReturn(TOKEN);

        ResponseEntity<?> response = authService.registration(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TOKEN, ((TokenResponse) ((StatusResponse) response.getBody()).getData()).getToken());
    }

    @Test
    public void testAuthorizationSuccess() {
        AuthRequest authRequest = getAuthRequestSupplier.get();
        Optional<User> oUser = Optional.of(getUserSupplier.get());
        User user = oUser.get();

        when(userService.getOUserByLogin(authRequest.getLogin())).thenReturn(oUser);
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtServiceUtils.generateToken(user)).thenReturn(TOKEN);

        ResponseEntity<?> response = authService.authorization(authRequest);

        assertNotNull(response);
        assertEquals(TOKEN, ((TokenResponse) ((StatusResponse) response.getBody()).getData()).getToken());
    }

    @Test
    public void testAuthorizationInvalidPassword() {
        AuthRequest authRequest = getAuthRequestSupplier.get();
        Optional<User> oUser = Optional.of(getUserSupplier.get());
        User user = oUser.get();

        when(userService.getOUserByLogin(authRequest.getLogin())).thenReturn(oUser);
        when(passwordEncoder.matches(authRequest.getPassword(), user.getPasswordHash())).thenReturn(false);

        ResponseEntity<?> response = authService.authorization(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User " + EMAIL + " not found or wrong password!", ((ErrorResponse) response.getBody()).getError().getMessage());
    }

    @Test
    public void testAuthorizationUserNotFound() {
        AuthRequest authRequest = getAuthRequestSupplier.get();
        Optional<User> oUser = Optional.empty();

        when(userService.getOUserByLogin(authRequest.getLogin())).thenReturn(oUser);

        ResponseEntity<?> response = authService.authorization(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User " + EMAIL + " not found or wrong password!", ((ErrorResponse) response.getBody()).getError().getMessage());
    }

    @Test
    public void testValidateTokenSuccess() {
        User user = getUserSupplier.get();

        when(userService.getUserByEmail(EMAIL)).thenReturn(user);

        ResponseEntity<?> response = authService.validate(EMAIL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USER", ((ValidResponse) ((StatusResponse) response.getBody()).getData()).getRoles().get(0));
    }
}
