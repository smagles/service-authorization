package ua.everybuy.authorization.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "sms_code", schema = "auth", catalog = "verceldb")
public class SmsCode {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "users_id", nullable = false)
    private long usersId;
    @Basic
    @Column(name = "code", nullable = false, length = 4)
    private String code;
    @Basic
    @Column(name = "at", nullable = false)
    private Timestamp at;
//    @OneToOne
//    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
//    private User usersByUserId;
}
