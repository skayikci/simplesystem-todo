package com.simplesystem.todo.controller.exception;

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
}
