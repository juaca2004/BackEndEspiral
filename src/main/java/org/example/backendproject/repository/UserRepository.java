package org.example.backendproject.repository;

import org.example.backendproject.Entity.Admin;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Admin,Long> {
    //CRUD
}
