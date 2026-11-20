// .java
// Spring JPA Line Item Schema
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.entities;
import jakarta.persistence.*;

/**
 * Line Item Schema
 */
@Entity
@Table(name = "line_items")
public class LineItem {
    /**
     * Columns
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_items_gen")
    @SequenceGenerator(name = "line_items_gen", sequenceName = "line_items_line_item_id_seq", allocationSize = 1)
    @Column(name = "line_item_id", nullable = false)
    Integer lineItemId;
    @Column(name = "invoice_number", nullable = false)
    Integer invoiceNumber;
    @Column(name = "item_number", nullable = true)
    Integer itemNumber;
    @Column(name = "qty", nullable = true)
    Integer qty;
    @Column(name = "discount", nullable = true)
    Float discount;

    /**
     * Constructors
     */
    public LineItem() {
    }
    public LineItem(Integer invoiceNumber, Integer itemNumber, Integer qty, Float discount) {
        this.invoiceNumber = invoiceNumber;
        this.itemNumber = itemNumber;
        this.qty = qty;
        this.discount = discount;
    }

    /**
     * Getters
     */
    public Integer getLineItemId() {
        return lineItemId;
    }
    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }
    public Integer getItemNumber() {
        return itemNumber;
    }
    public Integer getQty() {
        return qty;
    }
    public Float getDiscount() {
        return discount;
    }
}
