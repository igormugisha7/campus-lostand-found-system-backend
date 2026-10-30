package com.mugisha.campuslostandfound.repository;

import com.mugisha.campuslostandfound.entity.ReturnReceipt;
import com.mugisha.campuslostandfound.entity.ItemClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReturnReceiptRepository extends JpaRepository<ReturnReceipt, UUID> {

    @Query("SELECT e FROM ReturnReceipt e WHERE e.itemClaim.student.regNumber = :regNumber ORDER BY e.issuedDate DESC")
    List<ReturnReceipt> findByStudentRegNumberOrderByIssuedDateDesc(@Param("regNumber") String regNumber);

    boolean existsByItemClaim(ItemClaim itemClaim);
}
