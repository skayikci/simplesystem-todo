package com.simplesystem.todo.model;

import java.util.UUID;

public record TodoResponse(UUID id, String description, String createdDate, String dueDate, String doneDate,
                           TodoStatus status) {
}
