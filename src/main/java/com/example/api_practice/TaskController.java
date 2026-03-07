package com.example.api_practice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.api_practice.dto.TaskRequest;
import com.example.api_practice.dto.TaskResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService service;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(){
        log.info("GET /tasks called");
        List<TaskResponse> tasks = service.getAllTasks();

        return ResponseEntity.ok(
                ApiResponse.success(200, "Tasks retrieved successfully", tasks)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTask(@PathVariable Long id){
        log.info("GET /tasks/{} called", id);
        TaskResponse task = service.getTaskById(id);

        return ResponseEntity.ok(
                ApiResponse.success(200, "Task retrieved successfully", task)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest  request){
        log.info("POST /tasks title={}", request.getTitle());
        TaskResponse created = service.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Task created successfully", created)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request){

        log.info("PUT /tasks/{} called", id);
        TaskResponse updated = service.updateTask(id, request);

        return ResponseEntity.ok(
               ApiResponse.success(200, "Task updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id){
        log.info("DELETE /tasks/{} called", id);
        service.deleteTask(id);
        return  ResponseEntity.ok(
                ApiResponse.success(200, "Task deleted successfully", null)
        );
    }
}
