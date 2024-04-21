package org.example.backendproject;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.backendproject.Entity.Admin;
import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.ResponseRequest.*;
import org.example.backendproject.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.backendproject.repository.AdminRepository;

import java.util.ArrayList;

@CrossOrigin(maxAge = 3600)
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

    @GetMapping("doctor/listDoctors")
    public ResponseEntity<?> listDoctorPage(){

        var doctors = repositoryDoctor.findAll();
        return ResponseEntity.status(200).body(doctors);
    }



    //Crear un doctor
    @PostMapping("doctor/create")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor){
        var d = repositoryDoctor.searchByCc(doctor.getCc());
        if(d.isEmpty()) { //Es pq no encontro a nadie con la misma cedula
            doctor.setPassword("1234");
            repositoryDoctor.save(doctor);
            return ResponseEntity.status(200).body(new RegisterResponse("Nuevo doctor creado"));
        }
        else{
            return ResponseEntity.status(409).body(new RegisterResponse("El doctor ya existe en el sistema"));
        }
    }
    //Buscar doctores
    @GetMapping("doctor/search")
    public ResponseEntity<?> searchDoctor(@RequestBody String doctorCC) {
        return ResponseEntity.status(200).body(doctorCC);
    }

    //Eliminar doctores
    @DeleteMapping("doctor/delete/{cc}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("cc") String cc) {
        var doctor = repositoryDoctor.searchByCc(cc);
        if(doctor.isPresent()){
            Doctor doctorFound = doctor.get();
            repositoryDoctor.delete(doctorFound);
            System.out.println("doctor eliminado correctamente so");
            return ResponseEntity.status(200).body(doctorFound);
        }else{
            return ResponseEntity.status(400).body(new DeleteRequest("Usuario no encontrado"));
        }

    }

    //Lo coloco aqui pq no se pq no me dejo llamarlo dsd arriba
    public ArrayList<Doctor> listDoctorForDelete() {

        return (ArrayList<Doctor>) repositoryDoctor.findAll();
    }


    //Metodo de busqueda binaria mientras aprendemos filtrados por tablas jajaja (compara por cedula)
    public int binarySearch(Doctor[] array, String target) {
        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = array[mid].getCc().compareTo(target);
            if (cmp == 0) {
                return mid; // Encontrado
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // No encontrado
    }


    public int binarySearchDoctorName(Doctor[] array, String target) {
        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = array[mid].getName().compareTo(target);
            if (cmp == 0) {
                return mid; // Encontrado
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // No encontrado
    }

    //C(create )R (read ) U(update ) D (delete)


    @PostMapping("admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest){
        var admin= repositoryAdmin.searchByLogin(loginRequest.getUsername(),loginRequest.getPassword());
        if(admin.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect name or password");
        }else{
            return ResponseEntity.status(200).body(new LoginResponse("Welcome!"));
        }
    }

    private String generateToken(Admin admin) {
        String token = Jwts.builder()
                .setSubject(admin.getName())
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
                .compact();
        return token;
    }

    public int binarySearch(Admin[] array, String target) {
        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            System.out.println(array[mid].getName());
            System.out.println(target);
            int cmp = array[mid].getName().compareTo(target);
            if (cmp == 0) {
                return mid; // Encontrado
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // No encontrado
    }

    //Log in del doctor
    @PostMapping("doctor/login")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequest loginRequest){
        var doctor= repositoryDoctor.searchByLogin(loginRequest.getUsername(),loginRequest.getPassword());
        if(doctor.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect name or password");
        }else{
            return ResponseEntity.status(200).body(doctor.get());
        }
    }

    //Opción de cambiar contraseña

    @PostMapping("doctor/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        var optionalDoctor = repositoryDoctor.searchByLogin(changePasswordRequest.getUsername(), changePasswordRequest.getPassword());
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect name or password");
        } else {
            if(changePasswordRequest.getPasswordNEW1().equals(changePasswordRequest.getPasswordNEW2())){
                Doctor doctor = optionalDoctor.get();
                doctor.setPassword(changePasswordRequest.getPasswordNEW1());
               repositoryDoctor.save(doctor);
                return ResponseEntity.status(200).body("password changed");
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Both passwords are not the same");
            }

        }
    } 





}
