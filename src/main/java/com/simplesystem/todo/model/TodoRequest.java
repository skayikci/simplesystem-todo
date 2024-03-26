package com.simplesystem.todo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.NonNull;

public record TodoRequest(UUID id, @NonNull String description, LocalDateTime createdDate, LocalDateTime dueDate, LocalDateTime doneDate, TodoStatus status) {
}
