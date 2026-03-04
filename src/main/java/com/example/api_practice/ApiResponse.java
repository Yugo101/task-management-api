package com.example.api_practice;

public class ApiResponse<T> {
    private String status;
    private  T data;

    public ApiResponse(String status, T data){
        this.status = status;
        this.data = data;
    }

    public String getStatus(){
        return  status;
    }

    public T getData(){
        return data;
    }
}
