package br.com.knowledge.stockcontrol_api.shared.exception;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.knowledge.stockcontrol_api.product.exception.ProductNotFoundException;
import br.com.knowledge.stockcontrol_api.product.exception.ProductSkuAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
     * @ExceptionHandler(ConflictException.class)
     * public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException
     * exception, HttpServletRequest request) {
     * return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
     * }
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundException(ProductNotFoundException exception) {
        return new ErrorResponse(
                404,
                exception.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(ProductSkuAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleProductSkuAlreadyExistsException(ProductSkuAlreadyExistsException exception) {
        return new ErrorResponse(
                409,
                exception.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Os dados informados são inválidos.");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    public record ErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp) {
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }
}
