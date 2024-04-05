package org.example.backendproject.Entity;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;

public class Test {
    private Long id;
    private String date_taken;

    public Test(Long id, String date_taken) {
        this.id = id;
        this.date_taken = date_taken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public void setDate_taken(String date_taken) {
        this.date_taken = date_taken;
    }

    @OneToMany(mappedBy= "test")
    private List<Sample> samples;

    @OneToOne(mappedBy = "test")
    private List<Comments> comments;
}
