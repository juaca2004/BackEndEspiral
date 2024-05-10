package org.example.backendproject;


import org.example.backendproject.Entity.Comments;
import org.example.backendproject.Entity.Device;
import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.Entity.Patient;
import org.example.backendproject.ResponseRequest.*;
import org.example.backendproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    PatientRepository repositoryPatient;

    @Autowired
    MeditionRepository meditionRepository;

    @Autowired
    CommentsRepository commentsRepository;
    @Autowired
    DeviceRepository deviceRepository;


    @PostMapping("doctor")
    public ResponseEntity<?> signup(@RequestBody Doctor doctor) {
        doctor.setPassword("******");
        return ResponseEntity.status(200).body(doctor);
    }


    //Listar doctores
    @GetMapping("doctor/list")
    public ArrayList<Doctor> listDoctor() {
        return (ArrayList<Doctor>) repositoryDoctor.findAll();

    }

    @GetMapping("doctor/listDoctors")
    public ResponseEntity<?> listDoctorPage() {
        var doctors = repositoryDoctor.findAll();
        return ResponseEntity.status(200).body(doctors);
    }


    //Crear un doctor
    @PostMapping("doctor/create")
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        var d = repositoryDoctor.searchByCc(doctor.getCc());
        if (d.isEmpty()) { //Es pq no encontro a nadie con la misma cedula
            doctor.setPassword("1234");
            repositoryDoctor.save(doctor);
            return ResponseEntity.status(200).body(new RegisterResponse("Nuevo doctor creado"));
        } else {
            return ResponseEntity.status(409).body(new RegisterResponse("El doctor ya existe en el sistema"));
        }
    }

    //Buscar doctores
    @GetMapping("doctor/search/{cc}")
    public ResponseEntity<?> searchDoctor(@PathVariable("cc") String cc) {
        var doctor = repositoryDoctor.searchByCc(cc);
        if (doctor.isPresent()) {
            Doctor doctorFound = doctor.get();
            return ResponseEntity.status(200).body(doctorFound);
        } else {
            return ResponseEntity.status(400).body(new DeleteRequest("Usuario no encontrado"));
        }

    }

    //Eliminar doctores
    @DeleteMapping("doctor/delete/{cc}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("cc") String cc) {
        var doctor = repositoryDoctor.searchByCc(cc);
        if (doctor.isPresent()) {
            Doctor doctorFound = doctor.get();
            repositoryDoctor.delete(doctorFound);
            System.out.println("doctor eliminado correctamente so");
            return ResponseEntity.status(200).body(doctorFound);
        } else {
            return ResponseEntity.status(400).body(new DeleteRequest("Usuario no encontrado"));
        }

    }
    //C(create )R (read ) U(update ) D (delete)

    @PostMapping("admin/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequestAdmin loginRequest) {
        var admin = repositoryAdmin.searchByLogin(loginRequest.getUsername(), loginRequest.getPassword());
        if (admin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Incorrect name or password"));
        } else {
            return ResponseEntity.status(200).body(admin.get());
        }
    }

    //Log in del doctor
    @PostMapping("doctor/login")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequestDoctor loginRequest) {
        var doctor = repositoryDoctor.searchByLogin(loginRequest.getCC(), loginRequest.getPassword());
        if (doctor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Incorrect CC or password"));
        } else {
            return ResponseEntity.status(200).body(doctor.get());
        }
    }

    //Opción de cambiar contraseña

    @PostMapping("doctor/changePassword/{name}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        var optionalDoctor = repositoryDoctor.searchByLogin(changePasswordRequest.getCc(), changePasswordRequest.getPassword());
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.status(401).body("Incorrect cc or password");
        } else {
            if (changePasswordRequest.getPasswordNEW1().equals(changePasswordRequest.getPasswordNEW2())) {
                Doctor doctor = optionalDoctor.get();
                doctor.setPassword(changePasswordRequest.getPasswordNEW1());
                repositoryDoctor.save(doctor);
                return ResponseEntity.status(200).body("password changed");
            } else {
                return ResponseEntity.status(402).body("Both passwords are not the same");
            }

        }
    }
    //Filtrar pacientes
    @GetMapping("doctor/{doctorId}/filterPatients/{name}")
    public ResponseEntity<?> filterPatient(@PathVariable("name") String name, @PathVariable("doctorId") long doctorId) {
        var patients= repositoryPatient.filterByName(name, doctorId);
        if(patients.isEmpty()){
            return ResponseEntity.status(400).body(new filterPatientResponse("No matches"));
        }else{
            return ResponseEntity.status(200).body(patients);
        }
    }

    @GetMapping("patient/medition/{cc}")
    public ResponseEntity<?> filterMeditions(@PathVariable("cc") String cc){
        var meditions = meditionRepository.filterByPatientCC(cc);
        if(meditions.isEmpty()){
            return ResponseEntity.status(400).body(new filterMeditionsResponse("No matches"));
        }else{
            return ResponseEntity.status(200).body(meditions);
        }

    }
    @GetMapping("patient/medition/comments/{medicionid}")
    public  ResponseEntity<?> filterComments(@PathVariable("medicionid") long medicionid){
        var comments = commentsRepository.filterByCC(medicionid);
        return ResponseEntity.status(200).body(comments);

    }

    @PostMapping("patient/addmedition/{medicionid}")
    public ResponseEntity<?> addComment(@PathVariable("medicionid") Long meditionId, @RequestBody Comments comment){
        var medition = meditionRepository.serchById(meditionId);
        System.out.println(medition);
        if (medition.isEmpty()) {
            return ResponseEntity.status(404).body(new filterCommentsResponse("No hay medicion asociada"));
        } else{
            comment.setMedition(medition.get());
            medition.get().getComments().add(comment);
            commentsRepository.save(comment);
            meditionRepository.save(medition.get());

            return ResponseEntity.status(200).body(comment);
        }
    }

    @PostMapping("device/createDevice")
    public  ResponseEntity<?> createDevice(@RequestBody Device device){
        var d = deviceRepository.searchByName(device.getName());
        if (d.isEmpty()) {
            deviceRepository.save(device);
            return ResponseEntity.status(200).body(new RegisterResponse("new device created"));
        } else {
            return ResponseEntity.status(409).body(new RegisterResponse("this device already exists"));
        }
    }

    @GetMapping("device/list")
    public ArrayList<Device> listDevice() {
        return (ArrayList<Device>) deviceRepository.findAll();

    }

    @PostMapping("device/AssignDevice/{ccDoctor}")
    public ResponseEntity<?> assignDevice(@PathVariable("ccDoctor") String ccDoctor,@RequestBody Device device){
        var  d = repositoryDoctor.searchByCc(ccDoctor);

        if(d.isEmpty()){
            return ResponseEntity.status(404).body(new filterCommentsResponse("No existe el doctor"));
        }else{
            device.setDoctor(d.get());
            deviceRepository.save(device);
            Doctor doctor =d.get();
            doctor.getDevices().add(device);
            repositoryDoctor.save(doctor);
            return ResponseEntity.status(200).body(device);
        }
    }
}
