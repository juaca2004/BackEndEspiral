package org.example.backendproject.Entity;

import jakarta.persistence.OneToMany;

import java.util.List;

public class Patients {
    @OneToMany(mappedBy= "patient")
    private List<Test> tests;
}
