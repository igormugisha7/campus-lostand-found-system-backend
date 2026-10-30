package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.entity.ItemCategory;
import com.mugisha.campuslostandfound.repository.ItemCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;

    public ItemCategoryService(ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryRepository = itemCategoryRepository;
    }

    public String save(ItemCategory itemCategory) {
        itemCategoryRepository.save(itemCategory);
        return "Item category saved successfully";
    }

    public List<ItemCategory> findAll() {
        return itemCategoryRepository.findAll();
    }

    public Optional<ItemCategory> findById(UUID id) {
        return itemCategoryRepository.findById(id);
    }

    public Optional<ItemCategory> update(UUID id, ItemCategory newItemCategory) {
        Optional<ItemCategory> result = itemCategoryRepository.findById(id);
        if (result.isPresent()) {
            ItemCategory itemCategory = result.get();
            itemCategory.setName(newItemCategory.getName());
            return Optional.of(itemCategoryRepository.save(itemCategory));
        }
        return Optional.empty();
    }

    public String delete(UUID id) {
        if (itemCategoryRepository.existsById(id)) {
            itemCategoryRepository.deleteById(id);
            return "Item category deleted successfully";
        }
        return "The item category with that id does not exist";
    }

    public List<ItemCategory> findUnusedCategories() {
        return itemCategoryRepository.findUnusedCategories();
    }
}
