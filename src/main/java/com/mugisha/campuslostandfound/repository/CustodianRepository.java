package com.mugisha.campuslostandfound.repository;

import com.mugisha.campuslostandfound.entity.Custodian;
import com.mugisha.campuslostandfound.entity.FoundDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CustodianRepository extends JpaRepository<Custodian, UUID> {

    @Query("SELECT DISTINCT c FROM Custodian c JOIN c.itemCategories category WHERE LOWER(category.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Custodian> findByItemCategoryName(@Param("name") String name);

    boolean existsByFoundDesk(FoundDesk foundDesk);
}
