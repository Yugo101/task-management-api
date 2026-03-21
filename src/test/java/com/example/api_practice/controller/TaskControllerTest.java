package com.example.api_practice.controller;

import com.example.api_practice.dto.request.TaskRequest;
import com.example.api_practice.dto.response.TaskResponse;
import com.example.api_practice.exception.GlobalExceptionHandler;
import com.example.api_practice.exception.ResourceNotFoundException;
import com.example.api_practice.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("POST / tasks - タスク作成成功")
    void createTask_success() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test task");
        request.setDescription("Test Description");
        request.setCompleted(false);

        TaskResponse response = new TaskResponse(1L, "Test Task", "Test Description", false);

        given(taskService.createTask(any(TaskRequest.class))).willReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description") .value("Test Description"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("POST /tasks - バリデーションエラー")
    void createTask_validationError() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("");
        request.setDescription("Test Description");
        request.setCompleted(false);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/tasks"));
    }

    @Test
    @DisplayName("GET /tasks - タスク一覧取得成功")
    void getTasks_success() throws Exception {
        TaskResponse task1 = new TaskResponse(1L,"Task 1", "Description 1", false);
        TaskResponse task2 = new TaskResponse(2L, "Task 2", "Description 2", true);

        given(taskService.getTasks(any()))
                .willReturn(new PageImpl<>(
                        List.of(task1, task2),
                        PageRequest.of(0, 10),
                        2
                ));

            mockMvc.perform(get("/tasks")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(1))
                    .andExpect(jsonPath("$.content[0].title").value("Task 1"))
                    .andExpect(jsonPath("$.content[1].id").value(2))
                    .andExpect(jsonPath("$.content[1].title").value("Task 2"));
    }

    @Test
    @DisplayName("GET /tasks/{id} - タスク取得成功")
    void getTaskById_success() throws Exception {
        TaskResponse response = new TaskResponse(1L, "Task 1", "Description 1", false);

        given(taskService.getTaskById(1L)).willReturn(response);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("GET /tasks/{id} - 存在しないタスク")
    void getTaskBuId_notFound() throws Exception {
        given(taskService.getTaskById(9999L))
                .willThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(get("/tasks/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.path").value("/tasks/9999"));
    }

    @Test
    @DisplayName("PUT /tasks/{id} - タスク更新成功")
    void updateTask_success() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Task");
        request.setDescription("updated Description");
        request.setCompleted(true);

        TaskResponse response = new TaskResponse(1L, "Updated Task", "Updated Description", true);

        given(taskService.updateTask(eq(1L), any(TaskRequest.class))).willReturn(response);

        mockMvc.perform(put("/tasks/1")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    @DisplayName("PUT /tasks/{id} - 存在しないタスク")
    void updateTask_notFound() throws Exception{
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Task");
        request.setDescription("Updated Description");
        request.setCompleted(true);

        given(taskService.updateTask(eq(9999L), any(TaskRequest.class)))
                .willThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(put("/tasks/9999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.path").value("/tasks/9999"));
    }

    @Test
    @DisplayName("Put /tasks/{id} - バリデーションエラー")
    void updateTask_validationError() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("");
        request.setDescription("Updated Description");
        request.setCompleted(true);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/tasks/1"));
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - タスク削除成功")
    void deleteTask_success() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - 存在しないタスク")
    void deleteTask_notFound() throws Exception {
        doThrow(new ResourceNotFoundException("Task not found"))
                .when(taskService).deleteTask(9999L);

        mockMvc.perform(delete("/tasks/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.path").value("/tasks/9999"));
    }
}
