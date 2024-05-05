package org.example.backendproject.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comments {
    @Id
    private Long id;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "meditionID")
    private Medition medition;


    public Comments() {

    }

    public Comments(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medition getMedition() {
        return medition;
    }

    public void setMedition(Medition medition) {
        this.medition = medition;
    }
}
