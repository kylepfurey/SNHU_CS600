// .java
// Spring JPA Line Item Repository
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.repository;
import com.snhu.mscs.cs600.salesguru.entities.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Line Item Repository
 */
public interface LineItemRepository extends JpaRepository<LineItem, Integer> {
    /**
     * Queries
     */
    List<LineItem> findByInvoiceNumber(Integer invoiceNumber);
    List<LineItem> findByItemNumber(Integer itemNumber);
    List<LineItem> findByQtyBetween(Integer min, Integer max);
    List<LineItem> findByDiscountBetween(Float min, Float max);
}
