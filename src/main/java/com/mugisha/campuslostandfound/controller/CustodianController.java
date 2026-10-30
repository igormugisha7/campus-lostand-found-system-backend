package com.mugisha.campuslostandfound.controller;

import com.mugisha.campuslostandfound.entity.Custodian;
import com.mugisha.campuslostandfound.service.CustodianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/custodians")
public class CustodianController {

    private final CustodianService custodianService;

    public CustodianController(CustodianService custodianService) {
        this.custodianService = custodianService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Custodian custodian) {
        String message = custodianService.save(custodian);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public List<Custodian> findAll() {
        return custodianService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Custodian> result = custodianService.findById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The custodian with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Custodian custodian) {
        Optional<Custodian> result = custodianService.update(id, custodian);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The custodian with that id does not exist");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = custodianService.delete(id);
        if (message.equals("The custodian with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-item-category")
    public List<Custodian> findByItemCategoryName(@RequestParam String name) {
        return custodianService.findByItemCategoryName(name);
    }

    @PutMapping("/{custodianId}/item-categories/{itemCategoryId}")
    public ResponseEntity<String> addItemCategory(@PathVariable UUID custodianId,
                                                  @PathVariable UUID itemCategoryId) {
        String message = custodianService.addItemCategory(custodianId, itemCategoryId);
        if (message.equals("The custodian with that id does not exist")
                || message.equals("That item category does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("The custodian already has that item category")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{custodianId}/found-desk")
    public ResponseEntity<String> moveToFoundDesk(@PathVariable UUID custodianId,
                                                  @RequestParam int deskNumber) {
        String message = custodianService.moveToFoundDesk(custodianId, deskNumber);
        if (message.equals("The custodian with that id does not exist")
                || message.equals("The found desk with that deskNumber does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("That found desk is taken by another custodian")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }
}
