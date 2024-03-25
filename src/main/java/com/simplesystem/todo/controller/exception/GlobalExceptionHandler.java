package com.simplesystem.todo.controller.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
}
