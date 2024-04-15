package org.example.backendproject.repository;

import org.example.backendproject.Entity.Admin;
import org.example.backendproject.Entity.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin,Long> {


    @Query("SELECT u FROM Admin u WHERE u.name =:name AND u.password=:password")
    public Optional<Admin> searchByLogin(@Param("name") String name,@Param("password") String password);
}
