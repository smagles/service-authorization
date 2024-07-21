package ua.everybuy.authorization.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.everybuy.authorization.database.entity.AuditLog;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    @Query("SELECT userId FROM AuditLog WHERE actionId = :actionId")
    List<Long> findUserIdsByActionId(@Param("actionId") Long actionId);
}
