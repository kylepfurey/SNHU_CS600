// .java
// Spring JPA Item Repository
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.repository;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Item Repository
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {
    /**
     * Queries
     */
    List<Item> findByDescriptionContainingIgnoreCase(String description);
    List<Item> findByCostBetween(Float min, Float max);
    List<Item> findByRetailBetween(Float min, Float max);
}
