package org.example.backendproject;

import org.example.backendproject.Entity.Admin;
import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.example.backendproject.repository.AdminRepository;

import java.util.ArrayList;

@RestController
public class EchoController {

//Instanciar el repo

    @Autowired
    AdminRepository repositoryAdmin;

    @Autowired
    DoctorRepository repositoryDoctor;

    @PostMapping("doctor")
    public  ResponseEntity<?> signup(@RequestBody Doctor doctor){
        doctor.setPassword("******");
        return  ResponseEntity.status(200).body(doctor);
    }

    //Listar doctores
    @GetMapping("doctor/list")
    public ArrayList<Doctor> listDoctor(){
        return (ArrayList<Doctor>) repositoryDoctor.findAll();
    }

    //Crear un doctor
    @PostMapping("doctor/create")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor){
        repositoryDoctor.save(doctor); //Crear doctor
        return ResponseEntity.status(200).body("doctor creado");
    }
    //Buscar doctores
    @GetMapping("doctor/search")
    public ResponseEntity<?> searchDoctor(@RequestBody String doctorCC) {
        //Realizar algoritmo de busqueda binaria para la busqueda del doctor
        return null;
    }
    //Eliminar doctores
    @DeleteMapping("doctor/delete")
    public ResponseEntity<?> deleteDoctor(@RequestBody String doctorCC) {
        //Realizar algoritmo de busqueda binaria para la busqueda y eliminación del doctor
        return null;
    }
    //C(create )R (read ) U(update ) D (delete)

}
