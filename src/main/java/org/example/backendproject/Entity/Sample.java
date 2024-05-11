package org.example.backendproject.Entity;

import jakarta.persistence.*;
import org.example.backendproject.Entity.Medition;

@Entity
public class Sample {
    @Id
    private Long id;
    int time;
    int posX;
    int posY;
    int posZ;
    //Muchas muestras pueden ser de una medicion
    @ManyToOne()
    @JoinColumn(name = "MeditionID")
    private Medition medition;

    public Sample(int time, int posX, int posY, int posZ) {
        this.time = time;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public Sample() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public void setPosZ(int posZ) {
        this.posZ = posZ;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Medition getMedition() {
        return medition;
    }

    public void setMedition(Medition medition) {
        this.medition = medition;
    }
}
