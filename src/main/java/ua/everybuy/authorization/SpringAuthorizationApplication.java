package ua.everybuy.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringAuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAuthorizationApplication.class, args);
    }

}
