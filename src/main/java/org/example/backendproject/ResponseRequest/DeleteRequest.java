package org.example.backendproject.ResponseRequest;

public class DeleteRequest {

    private String doctorCc;

    public DeleteRequest(String doctorCc) {
        this.doctorCc = doctorCc;
    }

    public DeleteRequest() {
    }

    public String getDoctorCc() {
        return doctorCc;
    }

    public void setDoctorCc(String doctorCc) {
        this.doctorCc = doctorCc;
    }
}
