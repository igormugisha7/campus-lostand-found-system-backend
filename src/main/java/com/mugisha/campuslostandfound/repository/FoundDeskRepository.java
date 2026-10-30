package com.mugisha.campuslostandfound.repository;

import com.mugisha.campuslostandfound.entity.FoundDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoundDeskRepository extends JpaRepository<FoundDesk, UUID> {

    Optional<FoundDesk> findByDeskNumber(int deskNumber);

    @Query("SELECT e FROM FoundDesk e WHERE NOT EXISTS (SELECT c FROM Custodian c WHERE c.foundDesk = e) ORDER BY e.deskNumber ASC")
    List<FoundDesk> findFreeFoundDesks();
}
