package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.enums.ClaimStatus;
import com.mugisha.campuslostandfound.repository.CustodianRepository;
import com.mugisha.campuslostandfound.repository.ItemClaimRepository;
import com.mugisha.campuslostandfound.repository.ReturnReceiptRepository;
import com.mugisha.campuslostandfound.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final CustodianRepository custodianRepository;
    private final FoundDeskService foundDeskService;
    private final ItemClaimService itemClaimService;
    private final ReturnReceiptRepository returnReceiptRepository;

    public DashboardService(StudentRepository studentRepository,
                            CustodianRepository custodianRepository,
                            FoundDeskService foundDeskService,
                            ItemClaimService itemClaimService,
                            ReturnReceiptRepository returnReceiptRepository) {
        this.studentRepository = studentRepository;
        this.custodianRepository = custodianRepository;
        this.foundDeskService = foundDeskService;
        this.itemClaimService = itemClaimService;
        this.returnReceiptRepository = returnReceiptRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("students", studentRepository.count());
        summary.put("custodians", custodianRepository.count());
        summary.put("freeFoundDesks", (long) foundDeskService.findFreeFoundDesks().size());
        summary.put("claimsToday", itemClaimService.countClaimsToday());
        summary.put("reported", itemClaimService.countByStatus(ClaimStatus.REPORTED));
        summary.put("returnReceipts", returnReceiptRepository.count());
        return summary;
    }
}
