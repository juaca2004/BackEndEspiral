package org.example.backendproject.repository;

import org.example.backendproject.Entity.Comments;
import org.example.backendproject.Entity.Doctor;
import org.example.backendproject.Entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends CrudRepository<Comments,Long> {
    @Query("SELECT c FROM Comments c WHERE c.medition.id=:meditionid")
    public List<Comments> filterByCC(@Param("meditionid") long meditionid);

}


