package by.javaguru.jdmk13.securityservice.service.impl;

import by.javaguru.jdmk13.securityservice.model.SecurityRequestDto;
import by.javaguru.jdmk13.securityservice.model.SecurityResponseDto;
import by.javaguru.jdmk13.securityservice.service.SecurityService;
import by.javaguru.jdmk13.securityservice.utils.JsonInputProcess;
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
