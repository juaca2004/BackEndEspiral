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
import java.util.List;

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
        Iterable<Doctor> arraydoctor = repositoryDoctor.findAll();
        List<Doctor> doctorsList = new ArrayList<>();
        for (Doctor doctor : arraydoctor) {
            doctorsList.add(doctor);
        }


        //Realizar algoritmo de busqueda binaria para la busqueda del doctor

        return ResponseEntity.ok(binarySearchByCC(doctorsList, doctorCC));
    }

    public Doctor binarySearchByCC(List<Doctor> doctors, String cc) {
        int left = 0;
        int right = doctors.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Doctor midDoctor = doctors.get(mid);

            int ccComparison = cc.compareTo(midDoctor.getCc());

            // Check if cc is present at mid
            if (ccComparison == 0) {
                return midDoctor;
            }

            // If cc is greater, ignore left half
            if (ccComparison > 0) {
                left = mid + 1;
            } else { // If cc is smaller, ignore right half
                right = mid - 1;
            }
        }

        // If we reach here, then the cc was not present in the list
        return null;
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


    //C(create )R (read ) U(update ) D (delete)


    @PostMapping("admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest){
        var admin= repositoryAdmin.searchByLogin(loginRequest.getUsername(),loginRequest.getPassword());
        if(admin.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect name or password");
        }else{
            return ResponseEntity.status(200).body(admin.get());
        }
    }



    //Log in del doctor
    @PostMapping("doctor/login")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequest loginRequest){
        var doctor= repositoryDoctor.searchByLogin(loginRequest.getUsername(),loginRequest.getPassword());
        if(doctor.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect name or password");
        }else{
            return ResponseEntity.status(200).body(new LoginResponse("Welcome!"));
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
