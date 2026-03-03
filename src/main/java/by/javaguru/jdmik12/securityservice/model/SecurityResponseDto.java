package by.javaguru.jdmik12.securityservice.model;

public record SecurityResponseDto(long id,
                                  long requestId,
                                  boolean passed) {
}
