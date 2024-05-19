package org.example.backendproject;


import org.example.backendproject.Entity.*;
import org.example.backendproject.ResponseRequest.*;
import org.example.backendproject.Unity.DFTUtils;
import org.example.backendproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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

    @PostMapping("patient/create/{doctorId}")
    public ResponseEntity<?> createPatient(@PathVariable("doctorId") long doctorId, @RequestBody Patient patient) {
        var p = repositoryPatient.searchByCc(patient.getCc());
        var d= repositoryDoctor.getDoctor(doctorId);
        if (p.isEmpty() || d.isPresent()) { //Es pq nao encontro a nadie con la misma cedula
            Doctor doctor= d.get();
            patient.setDoctor(doctor);
            doctor.getPatients().add(patient);
            repositoryPatient.save(patient);
            return ResponseEntity.status(200).body(new RegisterResponse("Nuevo paciente creado"));
        } else {
            return ResponseEntity.status(409).body(new RegisterResponse("El paciente ya existe en el sistema"));
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
            System.out.println("doctor correctly removed so");
            return ResponseEntity.status(200).body(doctorFound);
        } else {
            return ResponseEntity.status(400).body(new DeleteRequest("The user isnot found"));
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
        var patients = repositoryPatient.filterByName(name, doctorId);
        if (patients.isEmpty()) {
            return ResponseEntity.status(400).body(new filterPatientResponse("No matches"));
        } else {
            return ResponseEntity.status(200).body(patients);
        }
    }
    @GetMapping("patient/medition/{cc}/{doctorId}")
    public ResponseEntity<?> filterMeditions(@PathVariable("cc") String cc,@PathVariable("doctorId") long doctorId) {
        var patient = repositoryPatient.filterByCC(cc, doctorId);

        if(patient.isEmpty()){
            return ResponseEntity.status(400).body(new filterPatientResponse("No matches in patient for doctor"));
        }else{
            var meditions = meditionRepository.filterByPatientCC(cc);
            if (meditions.isEmpty()) {
                return ResponseEntity.status(400).body(new filterMeditionsResponse("No matches in medition for patient"));
            } else {
                return ResponseEntity.status(200).body(meditions);
            }
        }
    }
    @GetMapping("patient/medition/getDevice/{id}/{deviceValue}")
    public ResponseEntity<?> FiltrerDevice(@PathVariable("id") long id,@PathVariable("deviceValue") String deviceValue) {
        var d = deviceRepository.filterByName(deviceValue, id);
        if(d.isEmpty()){
            return ResponseEntity.status(400).body(new filterDeviceResponse("No matches in device for doctor"));
        }else{
            Device device= d.get();
            return ResponseEntity.status(200).body(device);
        }
    }


    @GetMapping("patient/medition/comments/{meditionid}")
    public ResponseEntity<?> filterComments(@PathVariable("meditionid") long meditionid) {
        var comments = commentsRepository.filterByMeditionId(meditionid);
        return ResponseEntity.status(200).body(comments);

    }

    @PostMapping("patient/addmedition/{meditionid}")
    public ResponseEntity<?> addComment(@PathVariable("meditionid") Long meditionId, @RequestBody Comments comment) {
        var medition = meditionRepository.serchById(meditionId);
        System.out.println(medition);
        if (medition.isEmpty()) {
            return ResponseEntity.status(404).body(new filterCommentsResponse("There is no associated medication"));
        } else {
            comment.setMedition(medition.get());
            medition.get().getComments().add(comment);
            commentsRepository.save(comment);
            meditionRepository.save(medition.get());

            return ResponseEntity.status(200).body(comment);
        }
    }

    @PostMapping("device/createDevice")
    public ResponseEntity<?> createDevice(@RequestBody Device device) {
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



    @PostMapping("device/{idMedition}/measure")
    public ResponseEntity<?> receiveMeasurement(@RequestBody List<Sample> sampleRequestList, @PathVariable("idMedition") long idMedition) {
        // Loop through the received samples
        System.out.println("llegue");

        for (Sample sampleRequest : sampleRequestList) {
            Sample sample = new Sample();
            sample.setPosX(sampleRequest.getPosX());
            sample.setPosY(sampleRequest.getPosY());
            sample.setPosZ(sampleRequest.getPosZ());
            sample.setTime(sampleRequest.getTime());

            // Assuming Medition class has a method to find by ID
            Medition medition = meditionRepository.serchById(idMedition).orElse(null);
            if (medition == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medition not found");
            }

            sample.setMedition(medition);
            medition.getSamples().add(sample);

            // Save the changes
            meditionRepository.save(medition);
            sampleRepository.save(sample);
        }

        return ResponseEntity.ok("Measurements received and saved");
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
    public ResponseEntity<?> modifyPatients(@PathVariable("patientId") long patientId, @RequestBody Patient modifiedPatient) {
        var patientFound = repositoryPatient.getPatient(patientId);
        if (patientFound.isPresent()) {
            Patient p = patientFound.get();
            p.setName(modifiedPatient.getName());
            p.setEmail(modifiedPatient.getEmail());
            p.setPhone(modifiedPatient.getPhone());
            repositoryPatient.save(p);
            return ResponseEntity.status(200).body(new ModifyPatientRequest("Patient modified correctly"));
        } else {
            return ResponseEntity.status(400).body(new ModifyPatientRequest("Problem"));
        }
    }
    //Filtrado de mediciones de los pacientes (por CC) -> Solo pacientes asociados al doctor
    @GetMapping("doctor/{doctorId}/measurement/filterByCC/{patientCC}")
    public ResponseEntity<?> filterMeasurementByPatientName(@PathVariable("patientCC") String patientCC, @PathVariable("doctorId") long doctorId) {
        List<Medition> meditionsFiltered= meditionRepository.searchByPatientCC(doctorId,patientCC);
        if(meditionsFiltered.isEmpty()){
            return ResponseEntity.status(400).body(new filterMeditionsResponse("No matches"));
        }
        else{
            return ResponseEntity.status(200).body(meditionsFiltered);
        }
    }

    //Filtrado de mediciones de los pacientes (por nombre) -> Solo pacientes asociados al doctor
    @GetMapping("doctor/{doctorId}/measurement/filterByName/{patientName}")
    public ResponseEntity<?> filterPatientsByName(@PathVariable("patientName") String patientName, @PathVariable("doctorId") long doctorId) {
        List<Medition> meditionsFiltered = meditionRepository.filterByPatientName(patientName, doctorId);
        if(meditionsFiltered.isEmpty()){
            return ResponseEntity.status(400).body(new filterMeditionsResponse("No matches"));
        }
        else{
            return ResponseEntity.status(200).body(meditionsFiltered);
        }
    }
    //Filtrado de mediciones de los pacientes de un doctor (por fecha)  -> Solo pacientes asociados al doctor
    @GetMapping("doctor/{doctorId}/measurement/filterByDate/{dateA}/{dateB}")
    public ResponseEntity<?> filterMeasurementByDate(@PathVariable("dateA") Date dateA, @PathVariable("dateB") Date dateB, @PathVariable("doctorId") long doctorid) {
        List<Medition> meditionsFiltered = meditionRepository.filterByDate(dateA, dateB, doctorid);
        if (meditionsFiltered.isEmpty()) {
            return ResponseEntity.status(400).body(new filterMeditionsResponse("No matches"));
        } else {
            return ResponseEntity.status(200).body(meditionsFiltered);
        }
    }

    @GetMapping("doctor/patient/medition/{meditionid}")
    public ResponseEntity<?> getSamplesforMedition(@PathVariable("meditionid") Long meditionId){
       List<Sample> samples =sampleRepository.findByMeditionId(meditionId);

       if(samples.isEmpty()){
           System.out.println("nooooooo");
           return  ResponseEntity.status(404).body(new SamplesResponse("No Samples for this medition"));
       } else{
           double[] magnitudes = new double[samples.size()];
           long[] times = new long[samples.size()];

           for (int i = 0; i < samples.size(); i++) {
               Sample sample = samples.get(i);
               double magnitude = Math.sqrt(Math.pow(sample.getPosX(), 2) + Math.pow(sample.getPosY(), 2) + Math.pow(sample.getPosZ(), 2));

               magnitudes[i] = magnitude;
               times[i] = sample.getTime();
           }

           magnitudes = DFTUtils.normalize(magnitudes);
           double[] spectrum = DFTUtils.dftSpectrum(magnitudes);
           double[] freqs = DFTUtils.dftFreq(magnitudes, 50);

           ArraysGraphics arraysGraphics = new ArraysGraphics(magnitudes, times,spectrum,freqs);

           return ResponseEntity.status(200).body(arraysGraphics);

       }

    }
}
