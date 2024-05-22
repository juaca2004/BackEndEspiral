package org.example.backendproject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.util.List;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String cc;
    private String phone;
    private String email;
    //Se crea la relación muchos a uno (Muchos pacientes pueden tener un doctor)
    @ManyToOne()
    @JoinColumn(name = "DoctorID")
    private Doctor doctor;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Medition> meditions;

    public Patient(Long id, String name, String cc, String phone, String email) {
        this.id = id;
        this.name = name;
        this.cc = cc;
        this.phone = phone;
        this.email = email;
    }

    public Patient() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Medition> getMeditions() {
        return meditions;
    }

    public void setMeditions(List<Medition> meditions) {
        this.meditions = meditions;
    }
}
