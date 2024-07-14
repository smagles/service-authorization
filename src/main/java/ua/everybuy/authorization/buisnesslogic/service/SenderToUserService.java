package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.everybuy.authorization.database.entity.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class SenderToUserService {
    @Autowired
    private RestTemplate restTemplate;
    private final AuditLogService auditLogService;
    private final static String USER_SERVES_PASS_HEADER_PREFIX = "Service-Password";
    @Value("${user-service.create-url}")
    private String userCreateUrl;
    @Value("${user-service.password}")
    private String userServicePass;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void sendNewUserCreate(User user) {
        CompletableFuture.runAsync(() -> {
            try {
                String url = userCreateUrl + "?userId=" + user.getId();
                HttpHeaders headers = new HttpHeaders();
                ResponseEntity<String> response;

                headers.set(USER_SERVES_PASS_HEADER_PREFIX, userServicePass);

                HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
                response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if (response.getStatusCode().value() == 200) {
                    auditLogService.successSendUserIdToUserServ(user.getId());
                } //TODO ?
            } catch (Exception e) {
                //TODO
            }
        }, executorService);
    }
}
