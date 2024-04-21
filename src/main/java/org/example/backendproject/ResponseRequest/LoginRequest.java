package org.example.backendproject.ResponseRequest;

public class LoginRequest{

    private String CC;
    private String password;

    public LoginRequest(String CC, String password) {
        this.CC = CC;
        this.password = password;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}