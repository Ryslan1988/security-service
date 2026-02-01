package by.javaguru.jdmik12.securityservice.api.controllers;

import by.javaguru.jdmik12.securityservice.model.SecurityResponseDto;
import by.javaguru.jdmik12.securityservice.model.SecurityRequestDto;
import by.javaguru.jdmik12.securityservice.service.impl.SecurityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Security Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/security/check")
@Slf4j
public class SecurityApiController {

    private final SecurityServiceImpl accountingService;

    @Operation(summary = "Security auth")
    @ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "BAD REQUEST - Invalid input data")
    @PostMapping
    public ResponseEntity<SecurityResponseDto> check(@RequestBody SecurityRequestDto requestDto) throws IOException {
        log.debug("Received security request: {}", requestDto);
        SecurityResponseDto securityResponseDto = accountingService.getMockJsonById(requestDto);
        log.debug("Response security request: {}", securityResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(securityResponseDto);
    }

}
