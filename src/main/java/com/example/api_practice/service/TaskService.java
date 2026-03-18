package com.example.api_practice.service;

import com.example.api_practice.dto.request.TaskRequest;
import com.example.api_practice.dto.response.TaskResponse;
import com.example.api_practice.entity.Task;
import com.example.api_practice.exception.ResourceNotFoundException;
import com.example.api_practice.mapper.TaskMapper;
import com.example.api_practice.repository.TaskRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private static final Logger logger =
        LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        logger.info("Creating new task: {}", request.getTitle());

        Task task = TaskMapper.toEntity(request);
        Task savedTask = taskRepository.save(task);

        logger.info("Task created with id: {}", savedTask.getId());

        return TaskMapper.toResponse(savedTask);
    }

    public Page<TaskResponse> getTasks(Pageable pageable) {
        logger.info("Fetching tasks page: {}", pageable.getPageNumber());

        return taskRepository.findAll(pageable)
                .map(TaskMapper::toResponse);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return TaskMapper.toResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        logger.info("Updating task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        Task updated = taskRepository.save(task);

        return TaskMapper.toResponse(updated);
    }

    public void deleteTask(Long id) {
        logger.info("Deleting task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }
}
