package by.javaguru.jdmik12.securityservice.service.impl;

import by.javaguru.jdmik12.common.security.message.event.CheckSecurityEvent;
import by.javaguru.jdmik12.securityservice.model.SecurityResponseDto;
import by.javaguru.jdmik12.securityservice.model.SecurityRequestDto;
import by.javaguru.jdmik12.securityservice.service.SecurityService;
import by.javaguru.jdmik12.securityservice.utils.JsonInputProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final JsonInputProcess jsonInputProcess;

    @Override
    public CheckSecurityEvent getMockJsonById(Long requestId) throws IOException {
        return jsonInputProcess.jsonFileProcessUpdater(requestId);
    }

}
