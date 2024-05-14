package org.example.backendproject.repository;

import org.example.backendproject.Entity.Sample;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<Sample,Long> {

}
