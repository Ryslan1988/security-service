package by.javaguru.jdmik12.securityservice.service;

import by.javaguru.jdmik12.common.security.message.event.CheckSecurityEvent;

import java.io.IOException;

public interface SecurityService {

    CheckSecurityEvent getMockJsonById(Long requestId) throws IOException;

}