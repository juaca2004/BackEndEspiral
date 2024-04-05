package org.example.backendproject.Entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String cc;

    private String email;

    private String phone;

    private String password;

    @ManyToOne()
    @JoinColumn(name = "AdminID")
    private Admin admin;
    public Doctor() {
    }

    public Doctor(String name, String cc, String email, String phone, String password) {
        this.name = name;
        this.cc = cc;
        this.email = email;
        this.phone = phone;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @OneToMany(mappedBy = "doctor")
    private List<Patients> patients; //PREGUNTARLE A DOMI! -Val

}
