package com.simplesystem.todo.controller.exception;

import com.simplesystem.todo.model.TodoErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.badRequest().body(new TodoErrorResponse(HttpStatus.BAD_REQUEST.value(), invalidEntityException.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleInvalidEntityException(final EntityNotFoundException entityNotFoundException) {
        log.error("Handling entity not found exception: {}", entityNotFoundException.getMessage());
        return ResponseEntity.badRequest().body(new TodoErrorResponse(HttpStatus.BAD_REQUEST.value(), entityNotFoundException.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException illegalArgumentException) {
        log.error("Handling illegal argument exception: {}", illegalArgumentException.getMessage());
        return ResponseEntity.badRequest().body(new TodoErrorResponse(HttpStatus.BAD_REQUEST.value(), illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(final HttpMessageNotReadableException httpMessageNotReadableException) {
        var errorMessage = httpMessageNotReadableException.getMessage();
        log.error("Handling http message not readable exception: {}", httpMessageNotReadableException.getMessage());
        if (errorMessage.contains("Cannot deserialize value of type `com.simplesystem.todo.model.TodoStatus`")) {
            return ResponseEntity.badRequest().body(new TodoErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid todo status is provided, please check input"));
        }
        return ResponseEntity.badRequest().body(new TodoErrorResponse(HttpStatus.BAD_REQUEST.value(), httpMessageNotReadableException.getMessage()));
    }
}
