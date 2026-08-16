package br.com.knowledge.stockcontrol_api.product.exception;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {
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

    public record ErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp) {
    }
}
