package by.javaguru.jdmik12.securityservice.model.dto;

public record GenerateTokenRequest(
        long userId,
        String role
) {
}
