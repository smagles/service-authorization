package config;

import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.routing.dtos.RegistrationRequest;

import java.util.function.Supplier;

public class TestDataFactory {
    public final static String EMAIL = "test@mail.com";
    public final static String PHONE = "987654321";

    public static Supplier<RegistrationRequest> getRegistrationRequestSupplier = () -> {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(EMAIL);
        registrationRequest.setPhone(PHONE);
        registrationRequest.setPassword("P@ssw0rd");

        return registrationRequest;
    };

    public static Supplier<User> getUserSupplier = () -> {
        User user = new User();
        user.setId(1);
        user.setEmail(EMAIL);
        user.setPhoneNumber(PHONE);
        user.setBlock(false);

        return user;
    };
}
