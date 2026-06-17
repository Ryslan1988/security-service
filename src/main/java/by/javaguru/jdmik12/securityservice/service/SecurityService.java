package by.javaguru.jdmik12.securityservice.service;

import by.javaguru.jdmik12.securityservice.model.dto.CheckSecurityEvent;

import java.io.IOException;

public interface SecurityService {

    CheckSecurityEvent getMockJsonById(Long requestId) throws IOException;

}