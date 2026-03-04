package com.example.api_practice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.api_practice.dto.TaskRequest;
import com.example.api_practice.dto.TaskResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(){
        List<TaskResponse> tasks = service.getAllTasks();

        return ResponseEntity.ok(
                new ApiResponse<>("success", tasks)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTask(@PathVariable Long id){
        TaskResponse task = service.getTaskById(id);

        return ResponseEntity.ok(
                new ApiResponse<>("success", task)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest  request){
        TaskResponse created = service.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request){

        TaskResponse updated = service.updateTask(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>("success", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        service.deleteTask(id);
        return  ResponseEntity.noContent().build();
    }
}
