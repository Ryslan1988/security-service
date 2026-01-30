package by.javaguru.jdmk13.securityservice.controllers;

import by.javaguru.jdmk13.securityservice.model.SecurityRequestDto;
import by.javaguru.jdmk13.securityservice.model.SecurityResponseDto;
import by.javaguru.jdmk13.securityservice.service.impl.SecurityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "About Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/security/check")
public class SecurityApiController {

    private final SecurityServiceImpl accountingService;

    @Operation(summary = "Get about from CV")
    @ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - no token provided")
    @ApiResponse(responseCode = "400", description = "BAD REQUEST - Invalid input data")
    @PostMapping
    public ResponseEntity<SecurityResponseDto> check(@RequestBody @Valid SecurityRequestDto requestDto) throws IOException {
        SecurityResponseDto accountingResponseDto = accountingService.getMockJsonById(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountingResponseDto);
    }
}
