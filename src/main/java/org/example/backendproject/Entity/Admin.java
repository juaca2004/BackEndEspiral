package org.example.backendproject.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String password;

    //Se crea la relación 1 a muchos (Un admin puede tener muchos doctores)
    @OneToMany(mappedBy = "admin")
    private List<Doctor> doctors;

    public Admin() {

    }

    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
