package org.example.backendproject.ResponseRequest;

public class LoginRequestDoctor {

    private String CC;
    private String password;

    public LoginRequestDoctor(String CC, String password) {
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