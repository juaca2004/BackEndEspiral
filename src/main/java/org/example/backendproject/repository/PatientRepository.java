package org.example.backendproject.repository;
import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.Entity.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient,Long> {
    @Query("SELECT p FROM Patient p WHERE p.name =:name AND p.doctor.id=:doctorId")
    public List<Patient> filterByName(@Param("name") String name, @Param("doctorId") long doctorId);
    @Query("SELECT p FROM Patient p WHERE p.cc =:cc")
    public Optional<Patient> searchByCc(@Param("cc") String cc);


}