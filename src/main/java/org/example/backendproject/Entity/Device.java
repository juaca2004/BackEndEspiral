package org.example.backendproject.Entity;

import jakarta.persistence.*;
@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "deviceID")
    private Doctor doctor;


    public Device(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Device() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
