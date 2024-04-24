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
    @Id
    @Column(name = "users_id", nullable = false)
    private long usersId;
    @Basic
    @Column(name = "code", nullable = false, length = 4)
    private String code;
    @Basic
    @Column(name = "at", nullable = false, insertable = false)
    private Timestamp at;
//    @OneToOne
//    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
//    private User usersByUserId;
}
