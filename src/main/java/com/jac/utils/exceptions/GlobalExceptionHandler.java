package com.celesoft.utils.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiError>> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        List<FieldValidationError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldValidationError(err.getField(), err.getDefaultMessage()))
                .toList();

        String message = String.format("Validation failed for %d field(s)", fieldErrors.size());

        log.warn("Validation error at [{}]: {} | Fields: {}",
                exchange.getRequest().getPath().value(),
                message,
                fieldErrors.stream().map(FieldValidationError::toString).collect(Collectors.joining(", ")));

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(message)
                .errors(fieldErrors)
                .build();

        return Mono.just(ResponseEntity.badRequest().body(error));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ApiError>> handleNotFound(ResourceNotFoundException ex, ServerWebExchange exchange) {
        log.warn("Resource not found: {} | Path: {}", ex.getMessage(), exchange.getRequest().getPath().value());

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ApiError>> handleBusiness(BusinessException ex, ServerWebExchange exchange) {
        log.warn("⚡ Business exception: {} | Status: {} | Path: {}", ex.getMessage(), ex.getStatus(), exchange.getRequest().getPath().value());
        HttpStatus status = ex.getStatus() != null ? ex.getStatus(): HttpStatus.BAD_REQUEST;
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Business Error")
                .message(ex.getMessage())
                .build();
        return Mono.just(ResponseEntity.status(status).body(error));
    }

    @ExceptionHandler(org.springframework.web.server.MissingRequestValueException.class)
    public Mono<ResponseEntity<ApiError>> handleMissingRequestValue(
            org.springframework.web.server.MissingRequestValueException ex,
            ServerWebExchange exchange) {

        String message = ex.getReason() != null
                ? ex.getReason()
                : "Parámetro requerido no fue enviado.";

        log.warn("Missing request value at [{}]: {}",
                exchange.getRequest().getPath().value(), message);

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(message)
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler({
            org.springframework.data.mongodb.UncategorizedMongoDbException.class,
            org.springframework.data.mongodb.MongoTransactionException.class,
            com.mongodb.MongoException.class,
            com.mongodb.MongoWriteException.class,
            com.mongodb.MongoCommandException.class,
            com.mongodb.MongoWriteConcernException.class,
            com.mongodb.MongoTimeoutException.class,
            org.springframework.dao.DuplicateKeyException.class,
            org.springframework.dao.DataAccessException.class
    })
    public Mono<ResponseEntity<ApiError>> handleMongoErrors(Exception ex, ServerWebExchange exchange) {

        log.error("MongoDB error at [{}]: {}",
                exchange.getRequest().getPath().value(),
                ex.getMessage(),
                ex);

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .error("Database Error")
                .build();

        if (ex instanceof org.springframework.dao.DuplicateKeyException) {
            error.setStatus(HttpStatus.CONFLICT.value());
            error.setMessage("Registro duplicado. Verifique los datos enviados.");
            return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
        }

        if (ex instanceof com.mongodb.MongoTimeoutException) {
            error.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
            error.setMessage("Tiempo de espera agotado al comunicarse con la base de datos.");
            return Mono.just(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(error));
        }

        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("Error interno en la base de datos.");
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }



    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiError>> handleGeneral(Exception ex, ServerWebExchange exchange) {

        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Error interno inesperado. Por favor contacte al soporte técnico.")
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}
