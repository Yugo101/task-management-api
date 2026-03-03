package com.example.api_practice;

import org.springframework.stereotype.Service;
import java.util.List;
import com.example.api_practice.dto.TaskRequest;
import com.example.api_practice.dto.TaskResponse;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ))
                .toList();
    }

    public TaskResponse createTask(TaskRequest request){
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setCompleted(request.isCompleted());

        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.isCompleted()
        );
    }

    public TaskResponse updateTask(Long id, TaskRequest request){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.getTitle());
        task.setCompleted(request.isCompleted());

        Task updated = taskRepository.save(task);

        return new TaskResponse(
                updated.getId(),
                updated.getTitle(),
                updated.isCompleted()
        );
    }

    public void deleteTask(Long id){
        System.out.println("deleteTask called with id: " + id);
        if (!taskRepository.existsById(id)){
            System.out.println("Not found");
           throw new RuntimeException("Task not found");
        }

        taskRepository.deleteById(id);
    }
}
