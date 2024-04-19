package org.example.backendproject.ResponseRequest;

public class ChangePasswordRequest {
    private String username;
    private String password;
    private String passwordNEW1;
    private String passwordNEW2;

    public ChangePasswordRequest(String username, String password, String passwordNEW1, String passwordNEW2) {
        this.username = username;
        this.password = password;
        this.passwordNEW1 = passwordNEW1;
        this.passwordNEW2 = passwordNEW2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordNEW1() {
        return passwordNEW1;
    }

    public void setPasswordNEW1(String passwordNEW1) {
        this.passwordNEW1 = passwordNEW1;
    }

    public String getPasswordNEW2() {
        return passwordNEW2;
    }

    public void setPasswordNEW2(String passwordNEW2) {
        this.passwordNEW2 = passwordNEW2;
    }
}
