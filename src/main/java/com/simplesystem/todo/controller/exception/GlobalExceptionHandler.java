package com.simplesystem.todo.controller.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<Object> handleInvalidEntityException(final InvalidEntityException invalidEntityException) {
        log.error("Handling invalid entity exception: {}", invalidEntityException.getMessage());
        return ResponseEntity.badRequest().body(invalidEntityException.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleInvalidEntityException(final EntityNotFoundException entityNotFoundException) {
        log.error("Handling entity not found exception: {}", entityNotFoundException.getMessage());
        return ResponseEntity.badRequest().body(entityNotFoundException.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException illegalArgumentException) {
        log.error("Handling illegal argument exception: {}", illegalArgumentException.getMessage());
        return ResponseEntity.badRequest().body(illegalArgumentException.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(final HttpMessageNotReadableException httpMessageNotReadableException) {
        var errorMessage = httpMessageNotReadableException.getMessage();
        log.error("Handling http message not readable exception: {}", httpMessageNotReadableException.getMessage());
        if (errorMessage.contains("Cannot deserialize value of type `com.simplesystem.todo.model.TodoStatus`")) {
            return ResponseEntity.badRequest().body("Invalid todo status is provided, please check input");
        }
        return ResponseEntity.badRequest().body(httpMessageNotReadableException.getMessage());
    }
}
