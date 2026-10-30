package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.entity.Custodian;
import com.mugisha.campuslostandfound.entity.ItemClaim;
import com.mugisha.campuslostandfound.enums.ClaimStatus;
import com.mugisha.campuslostandfound.repository.ItemClaimRepository;
import com.mugisha.campuslostandfound.repository.ReturnReceiptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemClaimServiceTest {

    @Mock
    private ItemClaimRepository itemClaimRepository;

    @Mock
    private ReturnReceiptRepository returnReceiptRepository;

    @InjectMocks
    private ItemClaimService itemClaimService;

    @Test
    void saveRejectsPastDate() {
        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setClaimDate(LocalDate.now().minusDays(1));

        String result = itemClaimService.save(itemClaim);

        assertEquals("Item claim date cannot be in the past", result);
        verify(itemClaimRepository, never()).save(any(ItemClaim.class));
    }

    @Test
    void saveRejectsCustodianWithAnotherClaimThatDay() {
        Custodian custodian = new Custodian();
        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setClaimDate(LocalDate.now());
        itemClaim.setCustodian(custodian);
        when(itemClaimRepository.existsByCustodianAndClaimDateAndStatusNot(
                custodian, itemClaim.getClaimDate(), ClaimStatus.WITHDRAWN)).thenReturn(true);

        String result = itemClaimService.save(itemClaim);

        assertEquals("Custodian already handles a claim on that date", result);
        verify(itemClaimRepository, never()).save(any(ItemClaim.class));
    }

    @Test
    void saveAlwaysStartsClaimAsReported() {
        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setClaimDate(LocalDate.now());
        itemClaim.setStatus(ClaimStatus.RETURNED);
        when(itemClaimRepository.existsByCustodianAndClaimDateAndStatusNot(
                null, itemClaim.getClaimDate(), ClaimStatus.WITHDRAWN)).thenReturn(false);
        when(itemClaimRepository.save(itemClaim)).thenReturn(itemClaim);

        String result = itemClaimService.save(itemClaim);

        assertEquals("Item claim saved successfully", result);
        assertEquals(ClaimStatus.REPORTED, itemClaim.getStatus());
        verify(itemClaimRepository).save(itemClaim);
    }

    @Test
    void updateStatusAllowsOnlyNextStatus() {
        UUID id = UUID.randomUUID();
        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setStatus(ClaimStatus.REPORTED);
        when(itemClaimRepository.findById(id)).thenReturn(Optional.of(itemClaim));
        when(itemClaimRepository.save(itemClaim)).thenReturn(itemClaim);

        String result = itemClaimService.updateStatus(id, ClaimStatus.MATCHED);

        assertEquals("Item claim status updated successfully", result);
        assertEquals(ClaimStatus.MATCHED, itemClaim.getStatus());
    }

    @Test
    void updateStatusRejectsFinalStatus() {
        UUID id = UUID.randomUUID();
        ItemClaim itemClaim = new ItemClaim();
        itemClaim.setStatus(ClaimStatus.RETURNED);
        when(itemClaimRepository.findById(id)).thenReturn(Optional.of(itemClaim));

        String result = itemClaimService.updateStatus(id, ClaimStatus.REPORTED);

        assertEquals("Cannot change status from RETURNED to REPORTED", result);
        verify(itemClaimRepository, never()).save(any(ItemClaim.class));
    }
}
