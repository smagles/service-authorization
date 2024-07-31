package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
    @Value("${user-service.remove-url}")
    private String userRemoveUrl;
    @Value("${user-service.password}")
    private String userServicePass;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void sendNewUserCreate(long userId) {
        CompletableFuture.runAsync(() -> {
            try {
                String url = userCreateUrl + "?userId=" + userId;
                HttpHeaders headers = new HttpHeaders();
                ResponseEntity<String> response;

                headers.set(USER_SERVES_PASS_HEADER_PREFIX, userServicePass);

                HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
                response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if (response.getStatusCode().value() == 200) {
                    auditLogService.successSendUserIdToUserServ(userId);
                }
            } catch (RestClientResponseException e) {
                if (e.getStatusCode() == HttpStatus.CONFLICT) {
                    auditLogService.successSendUserIdToUserServ(userId);
                } else {
                    System.out.println("User-Service response: " + e.getMessage());  //TODO
                }
            }
        }, executorService);
    }

    public void sendUserRemove(long userId) {
        CompletableFuture.runAsync(() -> {
            try {
                String url = userRemoveUrl + "?userId=" + userId;
                HttpHeaders headers = new HttpHeaders();
                ResponseEntity<String> response;

                headers.set(USER_SERVES_PASS_HEADER_PREFIX, userServicePass);

                HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
                response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
                if (response.getStatusCode().value() == 200) {
                    auditLogService.successSendRemoveUserIdToUserServ(userId);
                }
            } catch (RestClientResponseException e) {
                if (e.getStatusCode() == HttpStatus.CONFLICT) {
                    auditLogService.successSendRemoveUserIdToUserServ(userId);
                } else {
                    System.out.println("User-Service response: " + e.getMessage());  //TODO
                }
            }
        }, executorService);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    private void scheduledSendUserIds() {
        List<Long> users = auditLogService.getUserIdsWithoutAction(1L);  //TODO

        for (Long u:users) {
            sendNewUserCreate(u);
        }
    }

    @Scheduled(cron = "0 30 3 * * ?")
    private void scheduledSendRemovedUserIds() {
        List<Long> users = auditLogService.getUserIdsWithActAndWithoutAct(3L, 2L);  //TODO

        for (Long u:users) {
            sendUserRemove(u);
        }
    }
}
