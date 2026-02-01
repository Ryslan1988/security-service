package by.javaguru.jdmik12.securityservice.service.impl;

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
    public SecurityResponseDto getMockJsonById(SecurityRequestDto requestDto) throws IOException {
        return jsonInputProcess.jsonFileProcessUpdater(requestDto.getRequestId());
    }

}
