package com.simplesystem.todo.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record TodoResponse(UUID id, String description, LocalDateTime createdDate, LocalDateTime dueDate, LocalDateTime doneDate, TodoStatus status) {
}
