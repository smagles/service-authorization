package ua.everybuy.authorization.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.everybuy.authorization.database.entity.SmsCode;
import ua.everybuy.authorization.database.repository.SmsCodeRepository;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsCodeService {
    @Value("${smsCode.lifetime}")
    private Duration smsCodeLifetime;
    private final SmsCodeRepository smsCodeRepository;
    private static final int CODE_LENGTH = 4;

    public String setSmsCode(Long userId) {
        String code = genCode(CODE_LENGTH);
        SmsCode smsCode = smsCodeRepository.findByUsersId(userId).orElseGet(SmsCode::new);

        if (smsCode.getUsersId() == 0) {
            smsCode.setUsersId(userId);
        }
        smsCode.setAt(Timestamp.from(Instant.now()));
        smsCode.setCode(code);
        smsCodeRepository.save(smsCode);

        return code;
    }

    public SmsCode getOSmsCode(Long userId) {
        return smsCodeRepository.findByUsersId(userId)
                .orElseThrow(()-> new EntityNotFoundException("Code not found!"));
    }

    public boolean isSmsCodeActual(SmsCode smsCode) {
        return smsCode.getAt().toInstant().plus(smsCodeLifetime).isAfter(Instant.now());
    }

    private String genCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }

    public void removeSmsCode(SmsCode smsCode) {
        smsCodeRepository.delete(smsCode);
    }
}
