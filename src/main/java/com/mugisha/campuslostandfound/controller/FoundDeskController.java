package com.mugisha.campuslostandfound.controller;

import com.mugisha.campuslostandfound.entity.FoundDesk;
import com.mugisha.campuslostandfound.service.FoundDeskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/found-desks")
public class FoundDeskController {

    private final FoundDeskService foundDeskService;

    public FoundDeskController(FoundDeskService foundDeskService) {
        this.foundDeskService = foundDeskService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody FoundDesk foundDesk) {
        String message = foundDeskService.save(foundDesk);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public List<FoundDesk> findAll() {
        return foundDeskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<FoundDesk> result = foundDeskService.findById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The found desk with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody FoundDesk foundDesk) {
        Optional<FoundDesk> result = foundDeskService.update(id, foundDesk);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The found desk with that id does not exist");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = foundDeskService.delete(id);
        if (message.equals("The found desk with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/free")
    public List<FoundDesk> findFreeFoundDesks() {
        return foundDeskService.findFreeFoundDesks();
    }
}
