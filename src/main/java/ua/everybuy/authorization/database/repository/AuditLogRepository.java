package ua.everybuy.authorization.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.everybuy.authorization.database.entity.AuditLog;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    @Query("SELECT id FROM User WHERE id not in(SELECT userId FROM AuditLog WHERE actionId = :actionId)")
    List<Long> getUsersWithoutAction(@Param("actionId") Long actionId);
    @Query("SELECT a.userId FROM AuditLog a WHERE a.userId = :haveActionId" +
             " AND a.userId not in(SELECT b.userId FROM AuditLog b WHERE b.userId = :haventActionId)")
    List<Long> getUserIdsWithActAndWithoutAct(@Param("haveActionId") Long haveActionId,
                                              @Param("haventActionId") Long haventActionId);
}
