package org.example.backendproject.ResponseRequest;

public class ModifyPatientRequest {
    String description;

    public ModifyPatientRequest(String description) {
        this.description = description;
    }

    public ModifyPatientRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

