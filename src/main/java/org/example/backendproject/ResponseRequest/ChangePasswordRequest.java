package org.example.backendproject.ResponseRequest;

public class ChangePasswordRequest {
    private String cc;
    private String password;
    private String passwordNEW1;
    private String passwordNEW2;

    public ChangePasswordRequest(String cc, String password, String passwordNEW1, String passwordNEW2) {
        this.cc = cc;
        this.password = password;
        this.passwordNEW1 = passwordNEW1;
        this.passwordNEW2 = passwordNEW2;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
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
