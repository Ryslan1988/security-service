package by.javaguru.jdmk13.securityservice.service;

import by.javaguru.jdmk13.securityservice.model.SecurityRequestDto;
import by.javaguru.jdmk13.securityservice.model.SecurityResponseDto;

import java.io.IOException;

public interface SecurityService {

    SecurityResponseDto getMockJsonById(SecurityRequestDto requestDto) throws IOException;

}