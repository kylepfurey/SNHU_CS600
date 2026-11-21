// .java
// Item API Service
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.snhu.mscs.cs600.salesguru.repository.ItemRepository;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import java.util.NoSuchElementException;

/**
 * API Service for Items
 */
@Service
public class ItemService {
    /**
     * Constants
     */
    public static final int DESCRIPTION_LENGTH = 128;

    /**
     * Repositories
     */
    final ItemRepository itemRepository;

    /**
     * Constructor
     */
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * API
     */
    @Transactional(readOnly = true)
    public List<Item> listItems() {
        return itemRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Item getItem(Integer itemNumber) {
        return itemRepository.findById(itemNumber)
                .orElseThrow(() -> new NoSuchElementException("itemNumber not found: " + itemNumber));
    }
    @Transactional
    public Item createItem(String description, Float cost, Float retail) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Blank description");
        }
        if (description.length() > DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description longer than " + DESCRIPTION_LENGTH + ": " + description.length());
        }
        validateMoney("cost", cost);
        validateMoney("retail", retail);
        return itemRepository.save(new Item(description.trim(), cost, retail));
    }

    /**
     * Helpers
     */
    void validateMoney(String name, Float value) {
        if (value == null || !Float.isFinite(value) || value < 0.0f) {
            throw new IllegalArgumentException(name + " must be a non-negative number");
        }
    }
}
