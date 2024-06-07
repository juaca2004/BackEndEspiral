package org.example.backendproject.repository;

import org.example.backendproject.Entity.Comments;
import org.example.backendproject.Entity.Device;
import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.Entity.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends CrudRepository<Device,Long> {

    @Query("SELECT u FROM Device u WHERE u. name =:name")
    public Optional<Device> searchByName(@Param("name") String name);

    @Query("SELECT d FROM Device d WHERE d.name like :name AND d.doctor.id=:doctorId")
    public Optional<Device>  filterByName(@Param("name") String name, @Param("doctorId")long doctorId);

    @Query("SELECT d FROM Device d WHERE  d.doctor.id=:doctorId")
    public List<Device> ListDivice(@Param("doctorId")long doctorId);

    @Override
    void delete(Device device);

}
