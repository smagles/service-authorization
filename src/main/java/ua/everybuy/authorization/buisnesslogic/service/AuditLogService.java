package ua.everybuy.authorization.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.AuditLog;
import ua.everybuy.authorization.database.repository.AuditLogRepository;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public void successSendUserIdToUserServ(Long userId) {
        AuditLog auditLog = new AuditLog();

        auditLog.setUserId(userId);
        try {
            auditLog.setIpV4(Inet4Address.getByName("0.0.0.0")); //TODO
        } catch (UnknownHostException e) {
            throw new RuntimeException(e); //TODO
        }
        auditLog.setActionId(1L); //TODO
        auditLogRepository.save(auditLog);
    }

    public List<Long> getSuccessSendUserIds() {
        return auditLogRepository.findUserIdsByActionId(1L); //TODO
    }
}
