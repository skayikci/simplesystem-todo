package com.simplesystem.todo.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.simplesystem.todo.controller.exception.InvalidEntityException;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity createTodoItemWithBody(@RequestBody TodoRequest todoRequest) throws InvalidEntityException {
        var createdDate = Optional.ofNullable(todoRequest.createdDate());
        if (todoRequest.dueDate() != null && todoRequest.dueDate().isBefore(createdDate.orElse(LocalDateTime.now()))) {
            throw new InvalidEntityException("Invalid entity, please check due date");
        }
        if (TodoStatus.PAST_DUE.equals(todoRequest.status())) {
            throw new InvalidEntityException("Invalid entity, please check input status");
        }
        var createdTodoItem = todoService.createTodo(todoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodoItem);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity getTodoItemById(@PathVariable String todoId) {
        var todoItemReturned = todoService.getTodoById(UUID.fromString(todoId));
        return ResponseEntity.status(HttpStatus.OK).body(todoItemReturned);
    }

    @PatchMapping
    public ResponseEntity updateTodoItemById(@RequestBody TodoRequest todoRequest) {
        var todoItemReturned = todoService.updateTodo(todoRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(todoItemReturned);
    }

    @GetMapping
    public ResponseEntity filterByTodoStatus(@RequestParam(value = "status", required = false) TodoStatus todoStatus) {
        var todoItemsReturned = todoService.filterByStatus(todoStatus);
        return ResponseEntity.status(HttpStatus.OK).body(todoItemsReturned);
    }

}
