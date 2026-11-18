// .java
// Spring JPA Item Schema
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.plus;
import jakarta.persistence.*;

/**
 * Item Schema
 */
@Entity
@Table(name = "items")
public class Item {
    /**
     * Columns
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_gen")
    @SequenceGenerator(name = "items_gen", sequenceName = "items_item_number_seq", allocationSize = 1)
    @Column(name = "item_number")
    Integer itemNumber;

    @Column(name = "description", nullable = true, length = 128)
    String description;

    @Column(name = "cost", nullable = true)
    Float cost;

    @Column(name = "retail", nullable = true)
    Float retail;

    /**
     * Constructors
     */
    public Item() {
    }
    public Item(String description, Float cost, Float retail) {
        this.description = description;
        this.cost = cost;
        this.retail = retail;
    }

    /**
     * Getters
     */
    public Integer getItemNumber() {
        return itemNumber;
    }
    public String getDescription() {
        return description;
    }
    public Float getCost() {
        return cost;
    }
    public Float getRetail() {
        return retail;
    }
}
