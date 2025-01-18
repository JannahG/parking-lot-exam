package com.example.JavaCodingExam.Controller.Response;

public class ErrorResponse {
    private int httpStatusCode;
    private String errorMessage;

    public ErrorResponse(int httpStatusCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
