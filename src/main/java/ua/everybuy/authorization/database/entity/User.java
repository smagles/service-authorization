package ua.everybuy.authorization.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "auth")
@RequiredArgsConstructor
public class User implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    @Basic
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    @Basic
    @Column(name = "registration_at", insertable = false, nullable = true)
    private Timestamp registrationAt;
    @Basic
    @Column(name = "phone_number", nullable = false, length = 12)
    private String phoneNumber;
    @Basic
    @Column(name = "is_block", insertable = false, nullable = false)
    private boolean isBlock;
    @Basic
    @Column(name = "password_reset_at", insertable = false, nullable = true)
    private Timestamp passwordResetAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return !isBlock;
    }
//    @OneToMany(mappedBy = "usersByUserId")
//    private Collection<AuditLog> auditLogsById;
//    @OneToMany(mappedBy = "usersByUserId")
//    private Collection<Session> sessionById;
//    @OneToOne(mappedBy = "usersByUsersId")
//    private SmsCode smsCodeById;
//    @OneToMany(mappedBy = "usersByUserId")
//    private Collection<UserRoles> userRolesById;

    @ManyToMany
    @JoinTable(name = "user_roles", schema = "auth",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

//    @Formula("'+380' + 'phone_number'")
//    private String fullNumber;
}
