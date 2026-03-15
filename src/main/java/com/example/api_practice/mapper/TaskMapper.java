package com.example.api_practice.mapper;

import com.example.api_practice.dto.request.TaskRequest;
import com.example.api_practice.dto.response.TaskResponse;
import com.example.api_practice.entity.Task;

public class TaskMapper {
    public static Task toEntity(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        return task;
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }
}
