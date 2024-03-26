package com.simplesystem.todo.util;

import java.util.UUID;

import com.simplesystem.todo.model.TodoResponse;
import com.simplesystem.todo.model.TodoStatus;

public final class TodoResponseBuilder {
    private UUID id;
    private String description;
    private String createdDate;
    private String dueDate;
    private String doneDate;
    private TodoStatus status;

    private TodoResponseBuilder() {
    }

    public static TodoResponseBuilder aTodoResponse() {
        return new TodoResponseBuilder();
    }

    public TodoResponseBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TodoResponseBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TodoResponseBuilder withCreatedDate(String createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TodoResponseBuilder withDueDate(String dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TodoResponseBuilder withDoneDate(String doneDate) {
        this.doneDate = doneDate;
        return this;
    }

    public TodoResponseBuilder withStatus(TodoStatus status) {
        this.status = status;
        return this;
    }

    public TodoResponse build() {
        return new TodoResponse(id, description, createdDate, dueDate, doneDate, status);
    }
}
