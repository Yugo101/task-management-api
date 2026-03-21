package com.example.api_practice.dto.request;

import com.example.api_practice.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequest {
    @NotBlank(message = "タイトルは必須です")
    @Size(max = 100, message = "タイトルは100文字以内です")
    private String title;

    @Size(max = 500, message = "descriptionは500文字以内です")
    private String description;

    private boolean completed;

    public TaskRequest(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }
    public TaskRequest() {}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
