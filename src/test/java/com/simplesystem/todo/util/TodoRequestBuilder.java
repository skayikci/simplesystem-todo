package com.simplesystem.todo.util;

import java.time.LocalDateTime;
import java.util.UUID;

import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoStatus;

public final class TodoRequestBuilder {
    private UUID id;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private LocalDateTime doneDate;
    private TodoStatus status;

    private TodoRequestBuilder() {
    }

    public static TodoRequestBuilder aTodoRequest() {
        return new TodoRequestBuilder();
    }

    public TodoRequestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TodoRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TodoRequestBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TodoRequestBuilder withDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TodoRequestBuilder withDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
        return this;
    }

    public TodoRequestBuilder withStatus(TodoStatus status) {
        this.status = status;
        return this;
    }

    public TodoRequest build() {
        return new TodoRequest(id, description, createdDate, dueDate, doneDate, status);
    }
}
