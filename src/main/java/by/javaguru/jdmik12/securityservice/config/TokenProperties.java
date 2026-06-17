package by.javaguru.jdmik12.securityservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record TokenProperties(
        String secret,
        long accessTokenExpiration,
        long refreshTokenExpiration
) {}
