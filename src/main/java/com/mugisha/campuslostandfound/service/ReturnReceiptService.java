package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.entity.ItemClaim;
import com.mugisha.campuslostandfound.entity.ReturnReceipt;
import com.mugisha.campuslostandfound.enums.ClaimStatus;
import com.mugisha.campuslostandfound.repository.ItemClaimRepository;
import com.mugisha.campuslostandfound.repository.ReturnReceiptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReturnReceiptService {

    private final ReturnReceiptRepository returnReceiptRepository;
    private final ItemClaimRepository itemClaimRepository;

    public ReturnReceiptService(ReturnReceiptRepository returnReceiptRepository,
                                ItemClaimRepository itemClaimRepository) {
        this.returnReceiptRepository = returnReceiptRepository;
        this.itemClaimRepository = itemClaimRepository;
    }

    public String save(UUID itemClaimId, ReturnReceipt returnReceipt) {
        Optional<ItemClaim> result = itemClaimRepository.findById(itemClaimId);
        if (result.isEmpty()) {
            return "The item claim with that id does not exist";
        }
        if (result.get().getStatus() != ClaimStatus.RETURNED) {
            return "Return receipts are only for RETURNED item claims";
        }

        returnReceipt.setItemClaim(result.get());
        returnReceipt.setIssuedDate(LocalDate.now());
        returnReceiptRepository.save(returnReceipt);
        return "Return receipt saved successfully";
    }

    public List<ReturnReceipt> findByStudentRegNumber(String regNumber) {
        return returnReceiptRepository.findByStudentRegNumberOrderByIssuedDateDesc(regNumber);
    }

    public String delete(UUID id) {
        if (returnReceiptRepository.existsById(id)) {
            returnReceiptRepository.deleteById(id);
            return "Return receipt deleted successfully";
        }
        return "The return receipt with that id does not exist";
    }
}
