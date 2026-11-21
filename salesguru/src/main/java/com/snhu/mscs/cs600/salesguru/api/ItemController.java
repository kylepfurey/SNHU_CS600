// .java
// Item API Controller
// by Kyle Furey
package com.snhu.mscs.cs600.salesguru.api;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import java.net.URI;
import java.util.List;

/**
 * REST API for Items
 */
@RestController
@RequestMapping("/api/v1")
public class ItemController {
    /**
     * Records
     */
    public record Request(String description, BigDecimal cost, BigDecimal retail) { }

    /**
     * Services
     */
    final ItemService itemService;

    /**
     * Constructor
     */
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * API
     */
    @GetMapping("/items")
    public List<Item> listItems() {
        return itemService.listItems();
    }
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(
            @RequestBody
            Request request
    ) {
        Item item = itemService.createItem(request.description(), request.cost(), request.retail());
        return ResponseEntity.created(URI.create("/api/v1/items/" + item.getItemNumber())).body(item);
    }
    @GetMapping("/items/{itemNumber}")
    public Item getItem(
            @PathVariable
            Integer itemNumber
    ) {
        return itemService.getItem(itemNumber);
    }
}
