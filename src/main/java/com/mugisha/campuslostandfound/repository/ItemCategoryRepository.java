package com.mugisha.campuslostandfound.repository;

import com.mugisha.campuslostandfound.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, UUID> {

    @Query("SELECT e FROM ItemCategory e WHERE e.custodians IS EMPTY")
    List<ItemCategory> findUnusedCategories();
}
