package com.example.api_practice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private int code;
    private  T data;
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    private ApiResponse(String status,int code, String message,
                        T data,Map<String, String> errors){
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp =LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>("success", code, message, data, null);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>("error", code, message, null, null);
    }

    public static <T> ApiResponse<T> validationError(int code, String message,
                                                    Map<String, String> errors){
        return new ApiResponse<>("error", code, message, null, errors);
    }


    public String getStatus(){
        return  status;
    }

    public T getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public int getCode() {
        return code;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
