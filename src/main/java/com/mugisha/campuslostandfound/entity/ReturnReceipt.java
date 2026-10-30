package com.mugisha.campuslostandfound.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "return_receipt")
public class ReturnReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String proofType;
    private String remarks;
    private int itemsReturned;
    private LocalDate issuedDate;

    @ManyToOne
    @JoinColumn(name = "item_claim_id")
    private ItemClaim itemClaim;

    public ReturnReceipt() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProofType() {
        return proofType;
    }

    public void setProofType(String proofType) {
        this.proofType = proofType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getItemsReturned() {
        return itemsReturned;
    }

    public void setItemsReturned(int itemsReturned) {
        this.itemsReturned = itemsReturned;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public ItemClaim getItemClaim() {
        return itemClaim;
    }

    public void setItemClaim(ItemClaim itemClaim) {
        this.itemClaim = itemClaim;
    }
}
