// .java
// Spring JPA Book Schema
// by Kyle Furey

package com.snhu.mscs.cs600.module2;
import jakarta.persistence.*;

/**
 * Book Schema
 */
@Entity
@Table(name = "books")
public class Book {
    /**
     * Columns
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, length = 75)
    String title;

    @Column(nullable = false, length = 75)
    String author;

    @Column(nullable = false)
    Integer year;

    /**
     * Constructors
     */
    protected Book() {
    }
    public Book(String title, String author, Integer year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    /**
     * Getters
     */
    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public Integer getYear() {
        return year;
    }
}
