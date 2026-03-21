package com.example.api_practice.service;

import com.example.api_practice.dto.request.TaskRequest;
import com.example.api_practice.dto.response.TaskResponse;
import com.example.api_practice.entity.Task;
import com.example.api_practice.exception.ResourceNotFoundException;
import com.example.api_practice.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("タスク作成成功")
    void createTask_success() {
        TaskRequest request = new TaskRequest("Test", "Desc", false);

        Task savedTask = new Task(1L, "Test", "Desc", false);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test");

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("IDでタスク取得成功")
    void getTaskById_success() {
        Task task = new Task(1L, "Test", "Desc", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L);

        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("IDでタスク取得失敗")
    void getTaskById_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found");
    }

    @Test
    @DisplayName("タスク一覧取得")
    void getTasks_success() {
        Task task = new Task(1L, "Test", "Desc", false);

        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<TaskResponse> result = taskService.getTasks(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("タスク更新成功")
    void updateTask_success() {
        Task existing = new Task(1L, "Old", "OldDesc", false);

        TaskRequest request = new TaskRequest("New", "NewDesc", true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.updateTask(1L, request);

        assertThat(response.getTitle()).isEqualTo("New");
        assertThat(response.getDescription()).isEqualTo("NewDesc");
        assertThat(response.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("タスク削除成功")
    void deleteTask_success() {
        Task task = new Task(1L, "Test", "Desc", false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("タスク削除失敗")
    void deleteTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
