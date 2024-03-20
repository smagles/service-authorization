package ua.everybuy.authorization.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "sessions", schema = "auth", catalog = "verceldb")
public class Session {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Basic
    @Column(name = "start_at", nullable = false)
    private Timestamp startAt;
    @Basic
    @Column(name = "ip_v4", nullable = true)
    private Object ipV4;
    @Basic
    @Column(name = "user_agent", nullable = true, length = -1)
    private String userAgent;
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    private User userByUserId;
}
