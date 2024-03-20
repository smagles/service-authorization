package ua.everybuy.authorization.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.authorization.database.entity.Role;
import ua.everybuy.authorization.database.entity.RoleList;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleList name);
}
