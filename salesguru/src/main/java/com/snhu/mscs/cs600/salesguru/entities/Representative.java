// .java
// Spring JPA Representative Schema
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.entities;
import jakarta.persistence.*;

/**
 * Representative Schema
 */
@Entity
@Table(name = "representative")
public class Representative {
    /**
     * Columns
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "representative_gen")
    @SequenceGenerator(name = "representative_gen", sequenceName = "representative_representative_id_seq", allocationSize = 10)
    @Column(name = "representative_id", nullable = false)
    Integer representativeId;
    @Column(name = "customer_number", nullable = true)
    Integer customerNumber;
    @Column(name = "first", nullable = true, length = 25)
    String first;
    @Column(name = "middle", nullable = true, length = 25)
    String middle;
    @Column(name = "last", nullable = true, length = 25)
    String last;
    @Column(name = "suffix", nullable = true, length = 15)
    String suffix;
    @Column(name = "salutation", nullable = true, length = 15)
    String salutation;
    @Column(name = "direct_line", nullable = true, length = 25)
    String directLine;
    @Column(name = "mobile", nullable = true, length = 25)
    String mobile;

    /**
     * Constructor
     */
    public Representative() {
    }
    public Representative(
            Integer representativeId,
            Integer customerNumber,
            String first,
            String middle,
            String last,
            String suffix,
            String salutation,
            String directLine,
            String mobile
    ) {
        this.representativeId = representativeId;
        this.customerNumber = customerNumber;
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.suffix = suffix;
        this.salutation = salutation;
        this.directLine = directLine;
        this.mobile = mobile;
    }

    /**
     * Getters
     */
    public Integer getRepresentativeId() {
        return representativeId;
    }
    public Integer getCustomerNumber() {
        return customerNumber;
    }
    public String getFirst() {
        return first;
    }
    public String getMiddle() {
        return middle;
    }
    public String getLast() {
        return last;
    }
    public String getSuffix() {
        return suffix;
    }
    public String getSalutation() {
        return salutation;
    }
    public String getDirectLine() {
        return directLine;
    }
    public String getMobile() {
        return mobile;
    }
}
