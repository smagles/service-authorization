package ua.everybuy.authorization.routing.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class KeepAliveController {
    @GetMapping("keep-alive")
    public String keepAlive() {
        return "Online!";
    }
}
