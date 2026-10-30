package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.entity.ItemClaim;
import com.mugisha.campuslostandfound.enums.ClaimStatus;
import com.mugisha.campuslostandfound.repository.ItemClaimRepository;
import com.mugisha.campuslostandfound.repository.ReturnReceiptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemClaimService {

    private final ItemClaimRepository itemClaimRepository;
    private final ReturnReceiptRepository returnReceiptRepository;

    public ItemClaimService(ItemClaimRepository itemClaimRepository,
                            ReturnReceiptRepository returnReceiptRepository) {
        this.itemClaimRepository = itemClaimRepository;
        this.returnReceiptRepository = returnReceiptRepository;
    }

    public String save(ItemClaim itemClaim) {
        if (itemClaim.getClaimDate().isBefore(LocalDate.now())) {
            return "Item claim date cannot be in the past";
        }

        if (itemClaimRepository.existsByCustodianAndClaimDateAndStatusNot(
                itemClaim.getCustodian(), itemClaim.getClaimDate(), ClaimStatus.WITHDRAWN)) {
            return "Custodian already handles a claim on that date";
        }

        itemClaim.setStatus(ClaimStatus.REPORTED);
        itemClaimRepository.save(itemClaim);
        return "Item claim saved successfully";
    }

    public List<ItemClaim> findAll() {
        return itemClaimRepository.findAll();
    }

    public Optional<ItemClaim> findById(UUID id) {
        return itemClaimRepository.findById(id);
    }

    public Optional<ItemClaim> update(UUID id, ItemClaim newItemClaim) {
        Optional<ItemClaim> result = itemClaimRepository.findById(id);
        if (result.isPresent()) {
            ItemClaim itemClaim = result.get();
            itemClaim.setClaimDate(newItemClaim.getClaimDate());
            itemClaim.setItemName(newItemClaim.getItemName());
            itemClaim.setDescription(newItemClaim.getDescription());
            itemClaim.setStudent(newItemClaim.getStudent());
            itemClaim.setCustodian(newItemClaim.getCustodian());
            return Optional.of(itemClaimRepository.save(itemClaim));
        }
        return Optional.empty();
    }

    public String delete(UUID id) {
        Optional<ItemClaim> result = itemClaimRepository.findById(id);
        if (result.isEmpty()) {
            return "The item claim with that id does not exist";
        }
        if (returnReceiptRepository.existsByItemClaim(result.get())) {
            return "That item claim has return receipts, delete them first";
        }
        itemClaimRepository.deleteById(id);
        return "Item claim deleted successfully";
    }

    public List<ItemClaim> findByStatus(ClaimStatus status) {
        return itemClaimRepository.findByStatusOrderByClaimDateAsc(status);
    }

    public List<ItemClaim> findBetween(LocalDate start, LocalDate end) {
        return itemClaimRepository.findByClaimDateBetweenOrderByClaimDateAsc(start, end);
    }

    public String updateStatus(UUID id, ClaimStatus newStatus) {
        Optional<ItemClaim> result = itemClaimRepository.findById(id);
        if (result.isEmpty()) {
            return "The item claim with that id does not exist";
        }

        ItemClaim itemClaim = result.get();
        ClaimStatus currentStatus = itemClaim.getStatus();
        boolean allowed = currentStatus == ClaimStatus.REPORTED
                && (newStatus == ClaimStatus.MATCHED || newStatus == ClaimStatus.WITHDRAWN);
        allowed = allowed || currentStatus == ClaimStatus.MATCHED
                && (newStatus == ClaimStatus.RETURNED || newStatus == ClaimStatus.WITHDRAWN);

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        itemClaim.setStatus(newStatus);
        itemClaimRepository.save(itemClaim);
        return "Item claim status updated successfully";
    }

    public List<Object[]> countClaimsByStatus() {
        return itemClaimRepository.countClaimsByStatus();
    }

    @Transactional
    public String cancelClaimsForCustodian(UUID custodianId, LocalDate date) {
        int count = itemClaimRepository.cancelClaimsForCustodian(custodianId, date);
        return count + " item claims cancelled";
    }

    public Page<ItemClaim> findPage(Pageable pageable) {
        return itemClaimRepository.findAll(pageable);
    }

    public long countByStatus(ClaimStatus status) {
        return itemClaimRepository.countByStatus(status);
    }

    public long countClaimsToday() {
        return itemClaimRepository.countByClaimDate(LocalDate.now());
    }
}
