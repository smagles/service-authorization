package ua.everybuy.authorization.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.everybuy.authorization.database.entity.SmsCode;

import java.util.Optional;

@Repository
public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {
    Optional<SmsCode> findByUsersId(Long userId);
}
