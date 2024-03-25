package com.simplesystem.todo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, UUID>, JpaSpecificationExecutor<Todo> {
    List<Todo> findByDueDateBefore(LocalDateTime currentDate);

    @Query("SELECT t FROM Todo t WHERE (:status IS NULL OR t.status = :status)")
    List<Todo> findByStatus(TodoStatus status);
}
