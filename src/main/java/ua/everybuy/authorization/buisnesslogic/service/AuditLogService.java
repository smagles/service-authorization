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

    private void addAuditLog(long userId, long actionId) {
        AuditLog auditLog = new AuditLog();

        auditLog.setUserId(userId);
        try {
            auditLog.setIpV4(Inet4Address.getByName("0.0.0.0")); //TODO
        } catch (UnknownHostException e) {
            throw new RuntimeException(e); //TODO
        }
        auditLog.setActionId(actionId);
        auditLogRepository.save(auditLog);
    }

    public void successSendUserIdToUserServ(long userId) {
        addAuditLog(userId, 1L); //TODO
    }

    public void successSendRemoveUserIdToUserServ(long userId) {
        addAuditLog(userId, 2L); //TODO
    }

    public void successRemoveUserAccount(long userId) {
        addAuditLog(userId, 3L); //TODO
    }

    public List<Long> getUserIdsWithActAndWithoutAct(long haveActId, long haventActId) {
        return auditLogRepository.getUserIdsWithActAndWithoutAct(haveActId, haventActId);
    }

    public List<Long> getUserIdsWithoutAction(Long actionId) {
        return auditLogRepository.getUsersWithoutAction(actionId);
    }
}
