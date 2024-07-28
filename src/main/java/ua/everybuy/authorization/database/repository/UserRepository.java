package ua.everybuy.authorization.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.everybuy.authorization.database.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String username);
    @Query("SELECT u FROM User u WHERE u.email = :login or u.phoneNumber = :login")
    Optional<User> findUserByLogin(String login);
    //List<User> findByEmailOrPhoneNumber(String email, String phone);
    //boolean existsByEmailOrPhoneNumber(String email, String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);
}
