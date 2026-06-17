package by.javaguru.jdmik12.securityservice.controller;

import by.javaguru.jdmik12.securityservice.model.TokenPair;
import by.javaguru.jdmik12.securityservice.model.dto.GenerateTokenRequest;
import by.javaguru.jdmik12.securityservice.model.dto.RefreshTokenRequest;
import by.javaguru.jdmik12.securityservice.model.dto.RevokeTokenRequest;
import by.javaguru.jdmik12.securityservice.model.dto.ValidateTokenRequest;
import by.javaguru.jdmik12.securityservice.service.TokenService;
import by.javaguru.jdmik12.securityservice.utils.SecurityValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/generate")
    public ResponseEntity<TokenPair> generate(@RequestBody GenerateTokenRequest request) {
        TokenPair tokenPair = tokenService.generateTokenPair(
                request.userId(),
                request.role()
        );

        return ResponseEntity.ok(tokenPair);
    }

    @PostMapping("/validate")
    public ResponseEntity<SecurityValidationResult> validate(@RequestBody ValidateTokenRequest request) {
        SecurityValidationResult result = tokenService.validate(request.token());

        if (!result.passed()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@RequestBody RefreshTokenRequest request) {
        TokenPair tokenPair = tokenService.refresh(request.refreshToken());

        return ResponseEntity.ok(tokenPair);
    }

    @PostMapping("/revoke")
    public ResponseEntity<Void> revoke(@RequestBody RevokeTokenRequest request) {
        tokenService.revoke(request.token());

        return ResponseEntity.noContent().build();
    }
}
