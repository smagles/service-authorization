package ua.everybuy.authorization.routing.controllers;

import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return "+";
    }

    @GetMapping("/check")
    public String check() {
        return "User role";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin role";
    }
}
