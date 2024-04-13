package org.example.backendproject.repository;

import org.example.backendproject.Entity.Doctor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepository extends CrudRepository<Doctor,Long> {
    //CRUD

    @Query("SELECT u FROM Doctor u WHERE u.cc =:cc")
    public Optional<Doctor>searchByCc(@Param("cc") String cc);

    @Override
    void delete(Doctor doctor);

}

