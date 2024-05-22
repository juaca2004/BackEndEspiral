package org.example.backendproject.repository;

import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.Entity.Medition;
import org.example.backendproject.Entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface MeditionRepository extends CrudRepository<Medition, Long> {
    @Query("SELECT m FROM Medition m WHERE m.patient.cc = :cc")
    List<Medition> filterByPatientCC(@Param("cc") String cc);

    @Query("SELECT m FROM Medition m WHERE m.id = :meditionId")
    public Optional<Medition> serchById(@Param("meditionId") long id);

    @Query("SELECT m FROM Medition m WHERE m.patient.name like %:name% and m.patient.doctor.id=:doctorId")
    List<Medition> filterByPatientName(@Param("name") String name, @Param("doctorId")long doctorId);
    @Query("SELECT m FROM Medition m WHERE m.dateTaken between :dateA AND :dateB and m.patient.doctor.id=:doctorid" )
    List<Medition> filterByDate(@Param("dateA") Date dateA, @Param("dateB")Date dateB, @Param("doctorid") long doctorid);

    @Query("SELECT m FROM Medition m WHERE m.patient.doctor.id = :idDoctor and m.patient.cc =:patientCC")
    List<Medition> searchByPatientCC(@Param("idDoctor") long idDoctor,@Param("patientCC") String patientCC);

    @Override
    void delete(Medition medition);
}