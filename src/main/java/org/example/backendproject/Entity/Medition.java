package org.example.backendproject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Medition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dateTaken;

    //Muchas mediciones pueden ser de un paciente
    @ManyToOne()
    @JoinColumn(name = "PatientID")
    private Patient patient;

    //Una medición puede tener muchas muestras
    @OneToMany(mappedBy = "medition")
    @JsonIgnore
    private List<Comments> comments;

    @OneToMany(mappedBy = "medition")
    @JsonIgnore
    private List<Sample> samples;


    public Medition(String dateTaken) {
        this.dateTaken = dateTaken;
    }
    public Medition() {
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
