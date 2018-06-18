package com.avanade.artmachina.app.models;

public class HttpResponseError {

    private int ErrorCode;

    private String ErrorMessage;

    public HttpResponseError(int errorCode, String errorMessage) {
        ErrorCode = errorCode;
        ErrorMessage = errorMessage;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }
}
