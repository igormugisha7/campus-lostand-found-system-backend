package com.mugisha.campuslostandfound.controller;

import com.mugisha.campuslostandfound.entity.ReturnReceipt;
import com.mugisha.campuslostandfound.service.ReturnReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/return-receipts")
public class ReturnReceiptController {

    private final ReturnReceiptService returnReceiptService;

    public ReturnReceiptController(ReturnReceiptService returnReceiptService) {
        this.returnReceiptService = returnReceiptService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID itemClaimId,
                                       @RequestBody ReturnReceipt returnReceipt) {
        String message = returnReceiptService.save(itemClaimId, returnReceipt);
        if (message.equals("The item claim with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("Return receipts are only for RETURNED item claims")) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/student/{regNumber}")
    public List<ReturnReceipt> findByStudent(@PathVariable String regNumber) {
        return returnReceiptService.findByStudentRegNumber(regNumber);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = returnReceiptService.delete(id);
        if (message.equals("The return receipt with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }
}
