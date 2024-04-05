package org.example.backendproject.repository;
import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.example.backendproject.repository.UserRepository;
@RestController
public class EchoController {

//Instanciar el repo

    @Autowired
    UserRepository repositoryAdmin;

    @Autowired
    DoctorRepository repositoryDoctor;


    //Cada metodo del controller estare en un URL difente
    @GetMapping("echo")
    public  String echo(){
        return "Conectado mi rey, desde wifi uwu";
    }

    @GetMapping("suma")
    public ResponseEntity<?> sum(@RequestParam("alfa") int alfa, @RequestParam("beta") int beta){
        return ResponseEntity.status(200).body(alfa+beta);
    }
    @PostMapping("doctor")
    public  ResponseEntity<?> signup(@RequestBody Doctor doctor){
        doctor.setPassword("******");
        return  ResponseEntity.status(200).body(doctor);

    }

    //Recibir un usuario
    @PostMapping("doctor/create")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor){
        //Guardar el usuario
        repositoryDoctor.save(doctor);

        return ResponseEntity.status(200).body("doctor creado");


    }

    //C(crete )R (reed ) U(update ) D (delete)
}
