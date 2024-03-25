package com.simplesystem.todo.model;

public record TodoErrorResponse(int httpStatusCode, String errorMessage) {
}
