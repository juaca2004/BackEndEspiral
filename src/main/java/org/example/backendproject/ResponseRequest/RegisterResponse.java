package org.example.backendproject.ResponseRequest;

public class RegisterResponse {
    String description;

    public RegisterResponse(String description) {
        this.description = description;
    }

    public RegisterResponse() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
