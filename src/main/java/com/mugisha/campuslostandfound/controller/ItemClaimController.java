package com.mugisha.campuslostandfound.controller;

import com.mugisha.campuslostandfound.entity.ItemClaim;
import com.mugisha.campuslostandfound.enums.ClaimStatus;
import com.mugisha.campuslostandfound.service.ItemClaimService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/item-claims")
public class ItemClaimController {

    private final ItemClaimService itemClaimService;

    public ItemClaimController(ItemClaimService itemClaimService) {
        this.itemClaimService = itemClaimService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ItemClaim itemClaim) {
        String message = itemClaimService.save(itemClaim);
        if (message.equals("Item claim date cannot be in the past")) {
            return ResponseEntity.badRequest().body(message);
        }
        if (message.equals("Custodian already handles a claim on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public List<ItemClaim> findAll() {
        return itemClaimService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<ItemClaim> result = itemClaimService.findById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The item claim with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ItemClaim itemClaim) {
        Optional<ItemClaim> result = itemClaimService.update(id, itemClaim);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The item claim with that id does not exist");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = itemClaimService.delete(id);
        if (message.equals("The item claim with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("That item claim has return receipts, delete them first")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-status")
    public List<ItemClaim> findByStatus(@RequestParam ClaimStatus status) {
        return itemClaimService.findByStatus(status);
    }

    @GetMapping("/between")
    public List<ItemClaim> findBetween(@RequestParam String start, @RequestParam String end) {
        return itemClaimService.findBetween(LocalDate.parse(start), LocalDate.parse(end));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam ClaimStatus status) {
        String message = itemClaimService.updateStatus(id, status);
        if (message.equals("The item claim with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.startsWith("Cannot change status")) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> countClaimsByStatus() {
        return itemClaimService.countClaimsByStatus();
    }

    @PatchMapping("/cancel-day")
    public String cancelClaimsForCustodian(@RequestParam UUID custodianId, @RequestParam String date) {
        return itemClaimService.cancelClaimsForCustodian(custodianId, LocalDate.parse(date));
    }

    @GetMapping("/page")
    public Page<ItemClaim> findPage(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "claimDate,desc") String sort) {
        String[] sortParts = sort.split(",", 2);
        String field = sortParts[0];
        Sort.Direction direction = Sort.Direction.DESC;
        if (sortParts.length > 1) {
            direction = Sort.Direction.fromString(sortParts[1]);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, field));
        return itemClaimService.findPage(pageable);
    }
}
