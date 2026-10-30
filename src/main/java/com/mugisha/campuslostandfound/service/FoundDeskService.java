package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.entity.FoundDesk;
import com.mugisha.campuslostandfound.repository.FoundDeskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FoundDeskService {

    private final FoundDeskRepository foundDeskRepository;

    public FoundDeskService(FoundDeskRepository foundDeskRepository) {
        this.foundDeskRepository = foundDeskRepository;
    }

    public String save(FoundDesk foundDesk) {
        foundDeskRepository.save(foundDesk);
        return "Found desk saved successfully";
    }

    public List<FoundDesk> findAll() {
        return foundDeskRepository.findAll();
    }

    public Optional<FoundDesk> findById(UUID id) {
        return foundDeskRepository.findById(id);
    }

    public Optional<FoundDesk> findByDeskNumber(int deskNumber) {
        return foundDeskRepository.findByDeskNumber(deskNumber);
    }

    public Optional<FoundDesk> update(UUID id, FoundDesk newFoundDesk) {
        Optional<FoundDesk> result = foundDeskRepository.findById(id);
        if (result.isPresent()) {
            FoundDesk foundDesk = result.get();
            foundDesk.setDeskNumber(newFoundDesk.getDeskNumber());
            foundDesk.setName(newFoundDesk.getName());
            return Optional.of(foundDeskRepository.save(foundDesk));
        }
        return Optional.empty();
    }

    public String delete(UUID id) {
        if (foundDeskRepository.existsById(id)) {
            foundDeskRepository.deleteById(id);
            return "Found desk deleted successfully";
        }
        return "The found desk with that id does not exist";
    }

    public List<FoundDesk> findFreeFoundDesks() {
        return foundDeskRepository.findFreeFoundDesks();
    }
}
