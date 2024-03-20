package ua.everybuy.authorization.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles", schema = "auth", catalog = "verceldb")
public class Role {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Enumerated(EnumType.STRING)
    private RoleList name;
//    @OneToMany(mappedBy = "rolesByRoleId")
//    private Collection<UserRoles> userRolesById;
}

