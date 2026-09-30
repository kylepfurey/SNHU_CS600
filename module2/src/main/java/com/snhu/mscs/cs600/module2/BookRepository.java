// .java
// Spring JPA Book Repository
// by Kyle Furey

package com.snhu.mscs.cs600.module2;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Book Repository
 */
public interface BookRepository extends JpaRepository<Book, Integer> {
    /**
     * Queries
     */
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByYear(Integer year);
}
