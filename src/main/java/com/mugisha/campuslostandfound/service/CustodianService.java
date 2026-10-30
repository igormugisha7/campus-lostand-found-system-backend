package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.entity.Custodian;
import com.mugisha.campuslostandfound.entity.FoundDesk;
import com.mugisha.campuslostandfound.entity.ItemCategory;
import com.mugisha.campuslostandfound.repository.CustodianRepository;
import com.mugisha.campuslostandfound.repository.FoundDeskRepository;
import com.mugisha.campuslostandfound.repository.ItemCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustodianService {

    private final CustodianRepository custodianRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final FoundDeskRepository foundDeskRepository;

    public CustodianService(CustodianRepository custodianRepository,
                            ItemCategoryRepository itemCategoryRepository,
                            FoundDeskRepository foundDeskRepository) {
        this.custodianRepository = custodianRepository;
        this.itemCategoryRepository = itemCategoryRepository;
        this.foundDeskRepository = foundDeskRepository;
    }

    public String save(Custodian custodian) {
        custodianRepository.save(custodian);
        return "Custodian saved successfully";
    }

    public List<Custodian> findAll() {
        return custodianRepository.findAll();
    }

    public Optional<Custodian> findById(UUID id) {
        return custodianRepository.findById(id);
    }

    public Optional<Custodian> update(UUID id, Custodian newCustodian) {
        Optional<Custodian> result = custodianRepository.findById(id);
        if (result.isPresent()) {
            Custodian custodian = result.get();
            custodian.setFirstName(newCustodian.getFirstName());
            custodian.setLastName(newCustodian.getLastName());
            custodian.setEmail(newCustodian.getEmail());
            custodian.setFoundDesk(newCustodian.getFoundDesk());
            custodian.setItemCategories(newCustodian.getItemCategories());
            return Optional.of(custodianRepository.save(custodian));
        }
        return Optional.empty();
    }

    public String delete(UUID id) {
        if (custodianRepository.existsById(id)) {
            custodianRepository.deleteById(id);
            return "Custodian deleted successfully";
        }
        return "The custodian with that id does not exist";
    }

    public List<Custodian> findByItemCategoryName(String name) {
        return custodianRepository.findByItemCategoryName(name);
    }

    @Transactional
    public String addItemCategory(UUID custodianId, UUID itemCategoryId) {
        Optional<Custodian> custodianResult = custodianRepository.findById(custodianId);
        if (custodianResult.isEmpty()) {
            return "The custodian with that id does not exist";
        }

        Optional<ItemCategory> categoryResult = itemCategoryRepository.findById(itemCategoryId);
        if (categoryResult.isEmpty()) {
            return "That item category does not exist";
        }

        Custodian custodian = custodianResult.get();
        ItemCategory itemCategory = categoryResult.get();
        if (custodian.getItemCategories().contains(itemCategory)) {
            return "The custodian already has that item category";
        }

        custodian.getItemCategories().add(itemCategory);
        custodianRepository.save(custodian);
        return "Item category added to the custodian";
    }

    public String moveToFoundDesk(UUID custodianId, int deskNumber) {
        Optional<Custodian> custodianResult = custodianRepository.findById(custodianId);
        if (custodianResult.isEmpty()) {
            return "The custodian with that id does not exist";
        }

        Optional<FoundDesk> deskResult = foundDeskRepository.findByDeskNumber(deskNumber);
        if (deskResult.isEmpty()) {
            return "The found desk with that deskNumber does not exist";
        }

        Custodian custodian = custodianResult.get();
        FoundDesk foundDesk = deskResult.get();
        boolean sameDesk = custodian.getFoundDesk() != null
                && custodian.getFoundDesk().getId().equals(foundDesk.getId());

        if (custodianRepository.existsByFoundDesk(foundDesk) && !sameDesk) {
            return "That found desk is taken by another custodian";
        }

        custodian.setFoundDesk(foundDesk);
        custodianRepository.save(custodian);
        return "Found desk changed successfully";
    }
}
