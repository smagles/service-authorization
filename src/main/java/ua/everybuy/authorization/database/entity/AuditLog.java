package ua.everybuy.authorization.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "audit_log", schema = "auth", catalog = "verceldb")
public class AuditLog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Basic
    @Column(name = "at", nullable = true)
    private Timestamp at;
    @Basic
    @Column(name = "ip_v4", nullable = true)
    private Object ipV4;
    @Basic
    @Column(name = "action_id", nullable = false)
    private long actionId;
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    private User userByUserId;
//    @ManyToOne
//    @JoinColumn(name = "action_id", referencedColumnName = "id", nullable = false)
//    private LogAction logActionByActionId;
}
