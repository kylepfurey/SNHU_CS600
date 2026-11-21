// .java
// Spring JPA Item Repository
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.repositories;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.math.BigDecimal;

/**
 * Item Repository
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {
    /**
     * Queries
     */
    List<Item> findByDescriptionContainingIgnoreCase(String description);
    List<Item> findByCostBetween(BigDecimal min, BigDecimal max);
    List<Item> findByRetailBetween(BigDecimal min, BigDecimal max);
}
