package by.javaguru.jdmik12.securityservice.service;

import by.javaguru.jdmik12.securityservice.model.SecurityResponseDto;
import by.javaguru.jdmik12.securityservice.model.SecurityRequestDto;

import java.io.IOException;

public interface SecurityService {

    SecurityResponseDto getMockJsonById(SecurityRequestDto requestDto) throws IOException;

}