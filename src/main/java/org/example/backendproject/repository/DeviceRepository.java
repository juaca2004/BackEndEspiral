package org.example.backendproject.repository;

import org.example.backendproject.Entity.Comments;
import org.example.backendproject.Entity.Device;
import org.example.backendproject.Entity.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends CrudRepository<Device,Long> {

    @Query("SELECT u FROM Device u WHERE u. name =:name")
    public Optional<Device> searchByName(@Param("name") String name);

}
