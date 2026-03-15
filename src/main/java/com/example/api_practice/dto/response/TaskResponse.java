package com.example.api_practice.dto.response;

public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    public TaskResponse(Long id, String title, String description, boolean completed){
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}
