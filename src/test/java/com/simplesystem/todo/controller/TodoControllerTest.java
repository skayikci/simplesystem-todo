package com.simplesystem.todo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simplesystem.todo.controller.adapter.LocalDateTimeTypeAdapter;
import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoResponse;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.service.TodoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TodoControllerTest {

    @MockBean
    TodoService todoService;

    @Autowired
    MockMvc mockMvc;

    private Todo.TodoBuilder getTodoRequestWithDate(UUID todoId, String description, LocalDateTime createdDate, LocalDateTime dueDate) {
        return getTodoRequest(description).id(todoId).createdDate(createdDate).dueDate(dueDate);
    }

    private Todo.TodoBuilder getTodoRequest(String description) {
        return Todo.builder().id(UUID.randomUUID()).description(description);
    }

    private TodoRequest generateTodoRequest(UUID mockTodoItemId, String description, LocalDateTime mockCreatedDate, LocalDateTime dueDate, TodoStatus todoStatus) {
        return new TodoRequest(mockTodoItemId, description, mockCreatedDate, dueDate, null, todoStatus);
    }

    private TodoResponse generateTodoResponse(UUID todoId, String description, LocalDateTime createdDate, LocalDateTime dueDate, TodoStatus status) {
        return new TodoResponse(todoId, description, createdDate, dueDate, null, status);
    }

    @Nested
    class GetByIdTests {
        @Test
        void shouldGetTodoItemById() throws Exception {
            var todoId = UUID.randomUUID();
            String description = "i will get this todo by id";
            LocalDateTime createdDate = LocalDateTime.now();
            LocalDateTime dueDate = createdDate.minusDays(-10L);
            TodoResponse todoResponse = generateTodoResponse(todoId, description, createdDate, dueDate, TodoStatus.NOT_DONE);
            String requestUrl = "/api/v1/todo/".concat(todoId.toString());
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .get(requestUrl)
                    .contentType(MediaType.APPLICATION_JSON);
            when(todoService.getTodoById(todoId)).thenReturn(todoResponse);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");


            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(equalTo(todoId.toString())))
                    .andExpect(jsonPath("$.description").value(equalTo(description)))
                    .andExpect(jsonPath("$.createdDate").value(equalTo(formatter.format(createdDate))))
                    .andExpect(jsonPath("$.dueDate").value(equalTo(formatter.format(dueDate))))
                    .andExpect(jsonPath("$.status").value(equalTo(TodoStatus.NOT_DONE.name())));


            verify(todoService, times(1)).getTodoById(todoId);
        }

        @Test
        void shouldGetEntityNotFoundExceptionWhenIdNotFound() throws Exception {
            var todoId = UUID.randomUUID();
            String requestUrl = "/api/v1/todo/".concat(todoId.toString());
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .get(requestUrl)
                    .contentType(MediaType.APPLICATION_JSON);
            when(todoService.getTodoById(todoId)).thenThrow(new EntityNotFoundException("Unable to find com.simplesystem.todo.model.Todo with id"));


            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value("Unable to find com.simplesystem.todo.model.Todo with id"))
                    .andExpect(jsonPath("$.httpStatusCode").value(400));

            verify(todoService, times(1)).getTodoById(todoId);
        }

        @Test
        void shouldGetIllegalArgumentExceptionWhenIdIsInvalid() throws Exception {
            var todoId = "0000";
            String requestUrl = "/api/v1/todo/".concat(todoId);
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .get(requestUrl)
                    .contentType(MediaType.APPLICATION_JSON);

            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value("Invalid UUID string: ".concat(todoId)))
                    .andExpect(jsonPath("$.httpStatusCode").value(400));

            verifyNoInteractions(todoService);
        }
    }

    @Nested
    class CreateTodoItemTests {
        @Test
        void shouldCreateATodoItemViaRestPOST() throws Exception {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            var todoId = UUID.randomUUID();
            String description = "i will do this via rest post";
            LocalDateTime createdDate = LocalDateTime.now();
            LocalDateTime dueDate = createdDate.plusDays(10L);
            TodoRequest todoRequest = generateTodoRequest(todoId, description, createdDate, dueDate, TodoStatus.NOT_DONE);
            String requestUrl = "/api/v1/todo";
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .post(requestUrl)
                    .content(gson.toJson(todoRequest))
                    .contentType(MediaType.APPLICATION_JSON);

            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isCreated());

            verify(todoService, times(1)).createTodo(any(TodoRequest.class));
        }

        @Test
        void shouldGetInvalidEntityExceptionWhenDueDateIsLessThanCreatedDate() throws Exception {
            var todoId = UUID.randomUUID();
            String description = "i will do this with old due date";
            LocalDateTime createdDate = LocalDateTime.now();
            LocalDateTime dueDate = createdDate.minusDays(10L);
            TodoRequest todoRequest = generateTodoRequest(todoId, description, createdDate, dueDate, TodoStatus.NOT_DONE);
            String requestUrl = "/api/v1/todo";
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .post(requestUrl)
                    .content(gson.toJson(todoRequest))
                    .contentType(MediaType.APPLICATION_JSON);


            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value("Invalid entity, please check due date"))
                    .andExpect(jsonPath("$.httpStatusCode").value(400));

            verifyNoInteractions(todoService);
        }

        @Test
        void shouldGetInvalidEntityExceptionWhenStatusIsProvidedAsPAST_DUEOnCreation() throws Exception {
            var todoId = UUID.randomUUID();
            String description = "i will do this with past_due creation status";
            LocalDateTime createdDate = LocalDateTime.now();
            LocalDateTime dueDate = createdDate.plusDays(10L);
            TodoRequest todoRequest = generateTodoRequest(todoId, description, createdDate, dueDate, TodoStatus.PAST_DUE);
            String requestUrl = "/api/v1/todo";
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .post(requestUrl)
                    .content(gson.toJson(todoRequest))
                    .contentType(MediaType.APPLICATION_JSON);


            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value("Invalid entity, please check input status"))
                    .andExpect(jsonPath("$.httpStatusCode").value(400));

            verifyNoInteractions(todoService);
        }

        @Test
        void shouldGetHttpMessageNotReadableExceptionWhenInvalidStatusProvided() throws Exception {
            String requestUrl = "/api/v1/todo";
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .post(requestUrl)
                    .content("""
                            {
                            	"description": "test with invalid status",
                            	"status": "INVALID"
                            }
                            """)
                    .contentType(MediaType.APPLICATION_JSON);


            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value("Invalid todo status is provided, please check input"))
                    .andExpect(jsonPath("$.httpStatusCode").value(400));

            verifyNoInteractions(todoService);
        }
    }

    @Nested
    class UpdateTodoItemTests {
        @Test
        void shouldUpdateATodoItemViaRestPATCH() throws Exception {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            var todoId = UUID.randomUUID();
            TodoRequest todoRequest = generateTodoRequest(todoId, "i will update this via rest post", null, null, null);
            TodoResponse todoResponse = generateTodoResponse(todoId, "i will update this via rest post", null, null, null);
            String requestUrl = "/api/v1/todo";
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .patch(requestUrl)
                    .content(gson.toJson(todoRequest))
                    .contentType(MediaType.APPLICATION_JSON);
            when(todoService.updateTodo(todoRequest)).thenReturn(todoResponse);


            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.id").value(equalTo(todoRequest.id().toString())))
                    .andExpect(jsonPath("$.description").value(equalTo(todoRequest.description())));

            verify(todoService, times(1)).updateTodo(any(TodoRequest.class));
        }

        @Test
        void shouldNotUpdateATodoWhenNotFound() throws Exception {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            TodoRequest todoRequest = generateTodoRequest(null, "i will update this via rest post",
                    null, null, null);
            String requestUrl = "/api/v1/todo";
            MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                    .patch(requestUrl)
                    .content(gson.toJson(todoRequest))
                    .contentType(MediaType.APPLICATION_JSON);
            when(todoService.updateTodo(todoRequest)).thenThrow(new EntityNotFoundException("Given entity with id not found"));


            mockMvc.perform(mockMvcRequestBuilders)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").value("Given entity with id not found"))
                    .andExpect(jsonPath("$.httpStatusCode").value(400));

            verify(todoService, times(1)).updateTodo(any(TodoRequest.class));
        }
    }
}
