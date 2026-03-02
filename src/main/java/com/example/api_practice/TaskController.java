package com.example.api_practice;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.api_practice.dto.TaskRequest;
import com.example.api_practice.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private final HandlerMapping resourceHandlerMapping;

    public TaskController(TaskService service, @Nullable HandlerMapping resourceHandlerMapping){
        this.service = service;
        this.resourceHandlerMapping = resourceHandlerMapping;
    }

    @GetMapping
    public Map<String, Object> getAllTasks(){
        List<TaskResponse> tasks = service.getAllTasks();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", tasks);

        return response;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTask(@Valid @RequestBody TaskRequest  request){
        TaskResponse task = service.createTask(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateTask(@PathVariable Long id,
                                   @Valid @RequestBody TaskRequest request){

        TaskResponse updated = service.updateTask(id, request);


        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", updated);

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        service.deleteTask(id);
        return  ResponseEntity.noContent().build();
    }
}
