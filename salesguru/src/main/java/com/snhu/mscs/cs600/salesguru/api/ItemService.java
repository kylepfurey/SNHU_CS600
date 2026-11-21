// .java
// Item API Service
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;
import com.snhu.mscs.cs600.salesguru.repositories.ItemRepository;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import java.util.NoSuchElementException;
import java.math.RoundingMode;

/**
 * API Service for Items
 */
@Service
public class ItemService {
    /**
     * Constants
     */
    public static final int DESCRIPTION_LENGTH = 128;
    public static final BigDecimal MONEY_MAX = new BigDecimal("9999999999.99"); // NUMERIC(12,2)

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
    public Item createItem(String description, BigDecimal cost, BigDecimal retail) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Blank description");
        }
        if (description.length() > DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description longer than " + DESCRIPTION_LENGTH + ": " + description.length());
        }
        return itemRepository.save(
                new Item(
                        description.trim(),
                        validateMoney("cost", cost),
                        validateMoney("retail", retail)
                )
        );
    }

    /**
     * Helpers
     */
    BigDecimal validateMoney(String name, BigDecimal value) {
        if (value == null || value.signum() < 0) {
            throw new IllegalArgumentException(name + " must be a non-negative number");
        }
        if (value.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException(name + " must have at most 2 decimal places");
        }
        if (value.compareTo(MONEY_MAX) > 0) {
            throw new IllegalArgumentException(name + " is too large");
        }
        return value.setScale(2, RoundingMode.UNNECESSARY);
    }
}
