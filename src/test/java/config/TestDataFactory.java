package config;

import ua.everybuy.authorization.database.entity.Role;
import ua.everybuy.authorization.database.entity.RoleList;
import ua.everybuy.authorization.database.entity.User;
import ua.everybuy.authorization.routing.dtos.AuthRequest;
import ua.everybuy.authorization.routing.dtos.RegistrationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TestDataFactory {
    public final static String EMAIL = "test@mail.com";
    public final static String PHONE = "987654321";
    public final static String PASSWORD = "P@ssw0rd";
    public final static String TOKEN = "validToken";
    public static Supplier<RegistrationRequest> getRegistrationRequestSupplier = () -> {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(EMAIL);
        registrationRequest.setPhone(PHONE);
        registrationRequest.setPassword(PASSWORD);

        return registrationRequest;
    };

    public static Supplier<List<Role>> getRolesList = () -> {
        List<Role> roleList = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setName(RoleList.USER);
        roleList.add(role);

        return roleList;
    };

    public static Supplier<User> getUserSupplier = () -> {
        User user = new User();
        user.setId(1);
        user.setEmail(EMAIL);
        user.setPhoneNumber(PHONE);
        user.setBlock(false);
        user.setRoles(getRolesList.get());

        return user;
    };

    public static Supplier<AuthRequest> getAuthRequestSupplier = () -> {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setLogin(EMAIL);
        authRequest.setPassword(PASSWORD);

        return authRequest;
    };
}
