package by.javaguru.jdmik12.securityservice.api.exception.handlers;

import by.javaguru.jdmik12.securityservice.api.exception.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.security.sasl.AuthenticationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String EUROPE_MINSK = "Europe/Minsk";

    @ExceptionHandler({BadRequestException.class, AuthenticationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException exception) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleMaxSizeException(MaxUploadSizeExceededException exception) {
        return new ErrorResponse(
                HttpStatus.EXPECTATION_FAILED.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowedException(HttpRequestMethodNotSupportedException exception) {
        return new ErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                exception.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException() {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON request",
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(constraintViolation -> constraintViolation
                .getPropertyPath()
                .forEach(error -> errors.put(constraintViolation.getMessage(), error.getName())));
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)));
    }
}
