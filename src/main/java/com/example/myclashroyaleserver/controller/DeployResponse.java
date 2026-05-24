package com.example.myclashroyaleserver.controller;

public class DeployResponse {
    private boolean success;
    private String message;

    public DeployResponse(boolean success,String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {return success;}
    public String getMessage() { return message; }
}
