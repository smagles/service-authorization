package service;

import static config.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import ua.everybuy.authorization.buisnesslogic.service.UserService;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.routing.dtos.ErrorResponse;
import ua.everybuy.authorization.routing.dtos.RegistrationRequest;
import ua.everybuy.authorization.routing.dtos.StatusResponse;
import ua.everybuy.authorization.routing.dtos.TokenResponse;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtServiceUtils jwtServiceUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        assertTrue(((ErrorResponse) response.getBody()).getError().getMessage().contains("Email " + EMAIL + " already in use"));
    }

    @Test
    public void testRegistrationPhoneAlreadyInUse() {
        RegistrationRequest request = getRegistrationRequestSupplier.get();

        when(userService.existsUserWithEmail(EMAIL)).thenReturn(false);
        when(userService.existsUserWithPhone(PHONE)).thenReturn(true);

        ResponseEntity<?> response = authService.registration(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(((ErrorResponse) response.getBody()).getError().getMessage().contains("Phone " + PHONE + " already in use"));
    }

    @Test
    public void testRegistrationSuccess() {
        RegistrationRequest request = getRegistrationRequestSupplier.get();
        User newUser = getUserSupplier.get();

        when(userService.existsUserWithEmail(EMAIL)).thenReturn(false);
        when(userService.existsUserWithPhone(PHONE)).thenReturn(false);
        when(passwordEncoder.encode("P@ssw0rd")).thenReturn("encodedPassword");
        when(userService.createNewUser(any(User.class))).thenReturn(newUser);
        when(jwtServiceUtils.generateToken(newUser)).thenReturn("jwtToken");

        ResponseEntity<?> response = authService.registration(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", ((TokenResponse) ((StatusResponse) response.getBody()).getData()).getToken());
    }
}
