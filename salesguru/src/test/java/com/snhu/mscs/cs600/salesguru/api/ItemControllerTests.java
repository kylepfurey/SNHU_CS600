// .java
// Item Controller Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.snhu.mscs.cs600.salesguru.config.ErrorHandler;
import org.junit.jupiter.api.Test;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import java.math.BigDecimal;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import java.util.NoSuchElementException;
import java.util.List;

/**
 * Item Controller Testing
 */
class ItemControllerTests {
    /**
     * Constants
     */
    final String json = "{\"description\":\"Pen\",\"cost\":1.00,\"retail\":2.50}";

    /**
     * Services
     */
    ItemService itemService;
    MockMvc mockMvc;

    /**
     * Item Controller Tests
     */
    @BeforeEach
    void reset() {
        itemService = mock(ItemService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ItemController(itemService))
                .setControllerAdvice(new ErrorHandler())
                .build();
    }
    @Test
    void createItemIs201() throws Exception {
        Item item = new Item("Pen", new BigDecimal("1.00"), new BigDecimal("2.50"));
        ReflectionTestUtils.setField(item, "itemNumber", 7);
        when(itemService.createItem(any(), any(), any()))
                .thenReturn(item);
        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status()
                        .isCreated())
                .andExpect(header()
                        .string("Location", "/api/v1/items/7"))
                .andExpect(jsonPath("$.description").
                        value("Pen"));
    }
    @Test
    void badItemIs400() throws Exception {
        when(itemService.createItem(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("bad"));
        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status()
                        .isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("bad"));
    }
    @Test
    void missingItemIs404() throws Exception {
        when(itemService.getItem(5))
                .thenThrow(new NoSuchElementException("itemNumber not found: 5"));
        mockMvc.perform(get("/api/v1/items/5"))
                .andExpect(status()
                        .isNotFound());
    }
    @Test
    void listItemsIs200() throws Exception {
        when(itemService.listItems())
                .thenReturn(List.of());
        mockMvc.perform(get("/api/v1/items"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.length()")
                        .value(0));
    }
}
