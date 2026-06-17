package by.javaguru.jdmik12.securityservice.utils;

import by.javaguru.jdmik12.securityservice.model.SecurityResponseDto;
import by.javaguru.jdmik12.securityservice.model.dto.CheckSecurityEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class JsonInputProcess {
    private final Random random = new Random();

    @Cacheable(value = "security", key = "#id")
    public CheckSecurityEvent jsonFileProcessUpdater(long id) {
        SecurityResponseDto dto = new SecurityResponseDto(
                random.nextInt(101), id, Math.random() < 0.5);

        return CheckSecurityEvent.builder()
                .withId(dto.id())
                .withRequestId(dto.requestId())
                .withIsPassed(dto.passed())
                .build();
    }
}
