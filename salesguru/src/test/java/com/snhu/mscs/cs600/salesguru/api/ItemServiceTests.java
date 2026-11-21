// .java
// Item Service Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import com.snhu.mscs.cs600.salesguru.repository.ItemRepository;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import com.snhu.mscs.cs600.salesguru.entities.Item;
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
        Item item = itemService.createItem("  Pen  ", 1.0f, 2.5f);
        assertEquals("Pen", item.getDescription());
        assertEquals(2.5f, item.getRetail());
    }
    @Test
    void rejectEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem(" ", 1.0f, 2.0f));
    }
    @Test
    void rejectLongDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("x".repeat(129), 1.0f, 2.0f));
    }
    @Test
    void rejectNegativeCost() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", -1.0f, 2.0f));
    }
    @Test
    void rejectNullRetail() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", 1.0f, null));
    }
    @Test
    void rejectInfiniteRetail() {
        assertThrows(IllegalArgumentException.class, () ->
                itemService.createItem("Pen", 1.0f, Float.POSITIVE_INFINITY));
    }
    @Test
    void throwOnMissingItem() {
        when(itemRepository.findById(5))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                itemService.getItem(5));
    }
}
