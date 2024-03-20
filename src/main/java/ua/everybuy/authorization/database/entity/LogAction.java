package ua.everybuy.authorization.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "log_actions", schema = "auth", catalog = "verceldb")
public class LogAction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "name", nullable = true, length = 32)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;
//    @OneToMany(mappedBy = "logActionsByActionId")
//    private Collection<AuditLog> auditLogsById;
}
