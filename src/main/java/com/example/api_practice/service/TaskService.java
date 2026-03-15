package com.example.api_practice.service;

import com.example.api_practice.dto.request.TaskRequest;
import com.example.api_practice.dto.response.TaskResponse;
import com.example.api_practice.entity.Task;
import com.example.api_practice.mapper.TaskMapper;
import com.example.api_practice.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = TaskMapper.toEntity(request);
        Task savedTask = taskRepository.save(task);

        return TaskMapper.toResponse(savedTask);
    }

    public Page<TaskResponse> getTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(TaskMapper::toResponse);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return TaskMapper.toResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        Task updated = taskRepository.save(task);

        return TaskMapper.toResponse(updated);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
