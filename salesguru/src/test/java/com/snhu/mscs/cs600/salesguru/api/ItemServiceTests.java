// .java
// Item Service Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import com.snhu.mscs.cs600.salesguru.repositories.ItemRepository;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import java.util.NoSuchElementException;

/**
 * Item Service Testing
 */
class ItemServiceTests {
    /**
     * Services
     */
    final ItemRepository itemRepository = mock(ItemRepository.class);
    final ItemService itemService = new ItemService(itemRepository);

    /**
     * Item Service Tests
     */
    @Test
    void createdItemIsValid() {
        when(itemRepository.save(any(Item.class)))
                .thenAnswer(call ->
                        call.getArgument(0));
        Item item = itemService.createItem("  Pen  ", new BigDecimal("1.00"), new BigDecimal("2.50"));
        assertEquals("Pen", item.getDescription());
        assertEquals(new BigDecimal("2.50"), item.getRetail());
    }
    @Test
    void rejectEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem(" ", new BigDecimal("1.00"), new BigDecimal("2.00")));
    }
    @Test
    void rejectLongDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("x".repeat(129), new BigDecimal("1.00"), new BigDecimal("2.00")));
    }
    @Test
    void rejectNegativeCost() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", new BigDecimal("-1.00"), new BigDecimal("2.00")));
    }
    @Test
    void rejectNullRetail() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", new BigDecimal("1.00"), null));
    }
    @Test
    void rejectTooManyDecimals() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", new BigDecimal("1.00"), new BigDecimal("2.005")));
    }
    @Test
    void throwOnMissingItem() {
        when(itemRepository.findById(5))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                itemService.getItem(5));
    }
    @Test
    void rejectNullCost() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", null, new BigDecimal("2.00")));
    }
    @Test
    void rejectAboveMoneyMax() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", new BigDecimal("1.00"), new BigDecimal("10000000000.00")));
    }
    @Test
    void acceptMaxLengthDescription() {
        when(itemRepository.save(any(Item.class)))
                .thenAnswer(call -> call.getArgument(0));
        assertDoesNotThrow(() ->
                itemService.createItem("x".repeat(128), new BigDecimal("1.00"), new BigDecimal("2.00")));
    }
}
