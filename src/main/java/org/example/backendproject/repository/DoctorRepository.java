package org.example.backendproject.repository;

import org.example.backendproject.Entity.Doctor;

import org.springframework.data.repository.CrudRepository;
public interface DoctorRepository extends CrudRepository<Doctor,Long> {
    //CRUD


    @Override
    void delete(Doctor doctor);

}

