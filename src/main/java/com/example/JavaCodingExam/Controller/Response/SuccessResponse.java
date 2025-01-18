package com.example.JavaCodingExam.Controller.Response;

import java.util.Optional;

public class SuccessResponse {
    private int statusCode;
    private String message;
    private Optional<Object> data;

    public SuccessResponse(int statusCode, String message, Optional<Object> data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Optional<Object> data) {
        this.data = data;
    }
}
