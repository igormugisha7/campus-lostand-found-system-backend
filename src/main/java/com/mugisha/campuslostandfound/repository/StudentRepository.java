package com.mugisha.campuslostandfound.repository;

import com.mugisha.campuslostandfound.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    List<Student> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT e.student FROM ItemClaim e WHERE e.custodian.id = :custodianId")
    List<Student> findStudentsOfCustodian(@Param("custodianId") UUID custodianId);
}
