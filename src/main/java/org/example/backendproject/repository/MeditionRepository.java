package org.example.backendproject.repository;

import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.Entity.Medition;
import org.example.backendproject.Entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeditionRepository extends CrudRepository<Medition, Long> {
    @Query("SELECT m FROM Medition m WHERE m.patient.cc = :cc")
    List<Medition> filterByPatientCC(@Param("cc") String cc);


    @Query("SELECT m FROM Medition m WHERE m.id = :meditionId")
    public Optional<Medition> serchById(@Param("meditionId") long id);

}