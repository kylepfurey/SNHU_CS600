// .java
// Spring JPA Company Schema
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.entities;
import jakarta.persistence.*;

/**
 * Company Schema
 */
@Entity
@Table(name = "company")
public class Company {
    /**
     * Columns
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_gen")
    @SequenceGenerator(name = "company_gen", sequenceName = "company_customer_number_seq", allocationSize = 10)
    @Column(name = "customer_number", nullable = false)
    Integer customerNumber;
    @Column(name = "name", nullable = true, length = 75)
    String name;
    @Column(name = "addr1", nullable = true, length = 75)
    String addr1;
    @Column(name = "addr2", nullable = true, length = 75)
    String addr2;
    @Column(name = "city", nullable = true, length = 40)
    String city;
    @Column(name = "state", nullable = true, length = 25)
    String state;
    @Column(name = "zip", nullable = true, length = 10)
    String zip;

    /**
     * Constructors
     */
    public Company() {
    }
    public Company(
            Integer customerNumber,
            String name,
            String addr1,
            String addr2,
            String city,
            String state,
            String zip
    ) {
        this.customerNumber = customerNumber;
        this.name = name;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    /**
     * Getters
     */
    public Integer getCustomerNumber() {
        return customerNumber;
    }
    public String getName() {
        return name;
    }
    public String getAddr1() {
        return addr1;
    }
    public String getAddr2() {
        return addr2;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZip() {
        return zip;
    }
}
