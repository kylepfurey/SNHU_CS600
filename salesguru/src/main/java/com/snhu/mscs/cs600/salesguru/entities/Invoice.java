// .java
// Spring JPA Invoice Schema
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Arrays;

/**
 * Invoice Schema
 */
@Entity
@Table(name = "invoice")
public class Invoice {
    /**
     * Columns
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_gen")
    @SequenceGenerator(name = "invoice_gen", sequenceName = "invoice_invoice_number_seq", allocationSize = 5)
    @Column(name = "invoice_number", nullable = false)
    Integer invoiceNumber;
    @Column(name = "customer_number", nullable = false)
    Integer customerNumber;
    @Column(name = "representative_id", nullable = false)
    Integer representativeId;
    @Column(name = "date_of_sale", nullable = false)
    LocalDate dateOfSale;
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "line_items", nullable = false)
    Integer[] lineItems = new Integer[0];

    /**
     * Constructors
     */
    public Invoice() {
    }
    public Invoice(
            Integer customerNumber,
            Integer representativeId,
            LocalDate dateOfSale,
            Integer[] lineItems
    ) {
        this.customerNumber = customerNumber;
        this.representativeId = representativeId;
        this.dateOfSale = dateOfSale;
        this.lineItems = lineItems;
    }

    /**
     * Getters
     */
    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }
    public Integer getCustomerNumber() {
        return customerNumber;
    }
    public Integer getRepresentativeId() {
        return representativeId;
    }
    public LocalDate getDateOfSale() {
        return dateOfSale;
    }
    public Integer[] getLineItems() {
        return lineItems.clone();
    }

    /**
     * Mutation
     */
    public void addLineItem(Integer lineItemId) {
        lineItems = Arrays.copyOf(lineItems, lineItems.length + 1);
        lineItems[lineItems.length - 1] = lineItemId;
    }
}
