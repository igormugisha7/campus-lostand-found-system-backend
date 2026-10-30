package com.mugisha.campuslostandfound.repository;

import com.mugisha.campuslostandfound.entity.Custodian;
import com.mugisha.campuslostandfound.entity.ItemClaim;
import com.mugisha.campuslostandfound.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ItemClaimRepository extends JpaRepository<ItemClaim, UUID> {

    List<ItemClaim> findByStatusOrderByClaimDateAsc(ClaimStatus status);

    List<ItemClaim> findByClaimDateBetweenOrderByClaimDateAsc(LocalDate start, LocalDate end);

    boolean existsByCustodianAndClaimDateAndStatusNot(Custodian custodian, LocalDate claimDate, ClaimStatus status);

    long countByStatus(ClaimStatus status);

    long countByClaimDate(LocalDate claimDate);

    @Query("SELECT e.status, COUNT(e) FROM ItemClaim e GROUP BY e.status")
    List<Object[]> countClaimsByStatus();

    @Modifying
    @Query("UPDATE ItemClaim e SET e.status = com.mugisha.campuslostandfound.enums.ClaimStatus.WITHDRAWN " +
            "WHERE e.custodian.id = :custodianId AND e.claimDate = :date " +
            "AND e.status <> com.mugisha.campuslostandfound.enums.ClaimStatus.RETURNED")
    int cancelClaimsForCustodian(@Param("custodianId") UUID custodianId, @Param("date") LocalDate date);

}
