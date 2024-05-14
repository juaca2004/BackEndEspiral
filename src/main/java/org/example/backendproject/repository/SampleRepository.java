package org.example.backendproject.repository;

import org.example.backendproject.Entity.Patient;
import org.example.backendproject.Entity.Sample;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SampleRepository extends CrudRepository<Sample,Long> {


    @Query("SELECT s FROM Sample s WHERE  s.medition.id=:meditionid")
    public List<Sample> findByMeditionId(@Param("meditionid")long meditionid);
}
