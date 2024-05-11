package org.example.backendproject;


import org.example.backendproject.Entity.*;
import org.example.backendproject.ResponseRequest.*;
import org.example.backendproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    SampleRepository sampleRepository;


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
    //Filtrar pacientes de un doctor
    @GetMapping("doctor/{doctorId}/filterPatients/{name}")
    public ResponseEntity<?> filterPatient(@PathVariable("name") String name, @PathVariable("doctorId") long doctorId) {
        var patients= repositoryPatient.filterByName(name, doctorId);
        if(patients.isEmpty()){
            return ResponseEntity.status(400).body(new filterPatientResponse("No matches"));
        }else{
            return ResponseEntity.status(200).body(patients);
        }
    }

    //Filtrar pacientes de la base de datos
    @GetMapping("doctor/filterPatients/{name}")
    public ResponseEntity<?> filterPatient(@PathVariable("name") String name) {
        var patients= repositoryPatient.filterByNameInDatabase(name);
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
        var DeviceOp = deviceRepository.searchByName(device.getName());

        if (d.isEmpty() || DeviceOp.isEmpty() ) {
            return ResponseEntity.status(404).body(new filterCommentsResponse("No existe el doctor"));
        } else {
            Doctor newDoctor = d.get();
            Device existingDevice=DeviceOp.get();
            if (existingDevice != null && existingDevice.getDoctor() != null) {
                Doctor oldDoctor = existingDevice.getDoctor();
                oldDoctor.getDevices().remove(existingDevice);
                repositoryDoctor.save(oldDoctor);
            }

            device.setDoctor(newDoctor);
            deviceRepository.save(device);
            newDoctor.getDevices().add(device);
            repositoryDoctor.save(newDoctor);
            return ResponseEntity.status(200).body(device);
        }
    }


    @GetMapping("device/{NameDevice}/startmeasureDevice")
    public ResponseEntity<?> startMeasurementDevice(@PathVariable("NameDevice") String NameDevice) {
        var d = deviceRepository.searchByName(NameDevice);
        if(d.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("device is not exist");
        }else{
            Device device= d.get();
            if (device.isMeasuring()) {
                return ResponseEntity.ok("Measurement started");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Measurement already in progress");
            }
        }

    }
    @GetMapping("device/{NameDevice}/stopmeasureDevice")
    public ResponseEntity<?> stopMeasurementDevice(@PathVariable("NameDevice") String NameDevice) {
        var d = deviceRepository.searchByName(NameDevice);
        if(d.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("device is not exist");
        }
        else{
            Device device= d.get();
            if (!device.isMeasuring()) {
                return ResponseEntity.ok("Measurement stopped");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No measurement in progress to stop");
            }
        }
    }

    @PostMapping("device/{xd}/{idmedition}/measure")
    public ResponseEntity<?> receiveMeasurement(@RequestBody Sample sampleRequest,@PathVariable("idmedition") long idmedition) {
        Sample sample = new Sample();
        sample.setPosX(sampleRequest.getPosX());
        sample.setPosY(sampleRequest.getPosY());
        sample.setPosZ(sampleRequest.getPosZ());
        sample.setTime(sampleRequest.getTime());
        Medition medition=meditionRepository.serchById(idmedition).get();
        sample.setMedition(medition);
        medition.getSamples().add(sample);
        meditionRepository.save(medition);
        sampleRepository.save(sample);
        return ResponseEntity.ok("Measurement received and saved");
    }

    @PostMapping("device/{NameDevice}/startMedition")
    public ResponseEntity<?> startMeasure(@PathVariable("NameDevice") String name) {
        var d=deviceRepository.searchByName(name);
        if(d.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("device is not exist");
        }else{
            Device device= d.get();
            device.setMeasuring(true);
            deviceRepository.save(device);
            return ResponseEntity.status(200).body("medition started");
        }

    }

    @PostMapping("device/{NameDevice}/stopMedition")
    public ResponseEntity<?> stopMeasure(@PathVariable("NameDevice") String name) {
        var d=deviceRepository.searchByName(name);
        if(d.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("device is not exist");
        }else{
            Device device= d.get();
            device.setMeasuring(false);
            deviceRepository.save(device);
            return ResponseEntity.status(200).body("medition stop");
        }

    }


    @PostMapping("medition/{ccPatient}/create")
    public ResponseEntity<?> createMedition(@PathVariable String ccPatient, @RequestBody Medition medition) {
        var p = repositoryPatient.searchByCc(ccPatient);
        if (!p.isEmpty()) { //Es pq no encontro a nadie con la misma cedula
            Patient patient= p.get();
            medition.setPatient(patient);
            patient.getMeditions().add(medition);
            repositoryPatient.save(patient);
            meditionRepository.save(medition);
            return ResponseEntity.status(200).body(medition);
        } else {
            return ResponseEntity.status(409).body(new RegisterResponse("El paciente no existe"));
        }
    }



    @PostMapping("doctor/patient/{patientId}/modify")
    public ResponseEntity<?> modifyPatients(@PathVariable("patientId") long patientId, @RequestBody Patient modifiedPatient){
        var patientFound = repositoryPatient.getPatient(patientId);
        if(patientFound.isPresent()){
            Patient p = patientFound.get();
            p.setName(modifiedPatient.getName());
            p.setCc(modifiedPatient.getCc());
            p.setEmail(modifiedPatient.getEmail());
            p.setPhone(modifiedPatient.getPhone());
            repositoryPatient.save(p);
            return ResponseEntity.status(200).body(new ModifyPatientRequest("Patient modified correctly"));
        }
        else{
            return ResponseEntity.status(400).body(new ModifyPatientRequest("Problem"));
        }
    }

}
