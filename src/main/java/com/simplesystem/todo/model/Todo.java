package com.simplesystem.todo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TODOS")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "ddMMyyyy HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "ddMMyyyy HH:mm:ss")
    private LocalDateTime dueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "ddMMyyyy HH:mm:ss")
    private LocalDateTime doneDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'NOT_DONE'")
    private TodoStatus status;
}