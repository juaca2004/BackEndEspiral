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
        ArrayList<Doctor> listOfDoctor = listDoctorForDelete();
        Doctor[] array = listOfDoctor.toArray(new Doctor[0]);
        int index = binarySearch(array, doctor.getCc());
        if (index != -1) {
            return ResponseEntity.ok("No se puede añadir, CC repetida");
        } else {
            repositoryDoctor.save(doctor);
            return ResponseEntity.status(200).body("doctor creado");
        }
    }
    //Buscar doctores
    @GetMapping("doctor/search")
    public ResponseEntity<?> searchDoctor(@RequestBody String doctorCC) {
        //Realizar algoritmo de busqueda binaria para la busqueda del doctor
        return ResponseEntity.status(200).body(doctorCC);
    }

    @DeleteMapping("doctor/d")
    public ResponseEntity<?> d(@RequestBody DeleteRequest request) {
        //Realizar algoritmo de busqueda binaria para la busqueda del doctor
        return ResponseEntity.status(200).body(request);
    }

    //Eliminar doctores
    @DeleteMapping("doctor/delete")
    public ResponseEntity<?> deleteDoctor(@RequestBody DeleteRequest doctorCc) {

        ArrayList<Doctor> listForDelete = listDoctorForDelete();
        Doctor[] array = listForDelete.toArray(new Doctor[0]);
        int index = binarySearch(array, doctorCc.getDoctorCc());
        if (index != -1) {
            repositoryDoctor.delete(array[index]);
            return ResponseEntity.ok("Doctor eliminado correctamente");
        } else {
             System.out.println("jajsjajsa");
            return ResponseEntity.notFound().build();
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
        ArrayList<Admin> list = (ArrayList<Admin>) repositoryAdmin.findAll();
        Admin[] array = list.toArray(new Admin[0]);
        int index = binarySearch(array,loginRequest.getUsername());
        if(index == -1){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nombre de usuario incorrecto");
        }else{
          if(list.get(index).getPassword().equals(loginRequest.getPassword())){
              String token = generateToken(list.get(index));
              return ResponseEntity.ok(new LoginResponse(token));
          }else {
              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
          }
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

    @PostMapping("doctor/login")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequest loginRequest){
        ArrayList<Doctor> list = (ArrayList<Doctor>) repositoryDoctor.findAll();
        Doctor[] array = list.toArray(new Doctor[0]);
        int index = binarySearchDoctorName(array,loginRequest.getUsername());
        if(index == -1){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nombre de usuario incorrecto");
        }else{
            if(list.get(index).getPassword().equals(loginRequest.getPassword())){
                String token = generateToken(list.get(index));
                return ResponseEntity.ok(new LoginResponse(token));
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        }
    }

    private String generateToken(Doctor doctor) {
        String token = Jwts.builder()
                .setSubject(doctor.getName())
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
                .compact();
        return token;
    }



}
