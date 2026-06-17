package by.javaguru.jdmik12.securityservice.utils;

public record SecurityValidationResult(
        boolean passed,
        Long userId,
        String role,
        String reason
) {
    public static SecurityValidationResult passed(long userId, String role) {
        return new SecurityValidationResult(true, userId, role, null);
    }

    public static SecurityValidationResult rejected(String reason) {
        return new SecurityValidationResult(false, null, null, reason);
    }
}
