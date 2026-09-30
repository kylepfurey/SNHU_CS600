// .java
// Spring JPA Book Repository Test
// by Kyle Furey

package com.snhu.mscs.cs600.module2;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;

/**
 * Test Book Repository
 */
@SpringBootTest
class BookRepositoryTest {
    /**
     * Repository
     */
    @Autowired
    BookRepository repo;

    /**
     * Tests
     */
    @Test
    void testQueries() {
        Assertions.assertEquals(100, repo.count());
        Assertions.assertEquals(2, repo.findByAuthor("Jane Austen").size());
        Assertions.assertFalse(repo.findByTitle("Dracula").isEmpty());
        List<Book> from1813 = repo.findByYear(1813);
        Assertions.assertEquals(1, from1813.size());
        Assertions.assertEquals("Pride and Prejudice", from1813.getFirst().getTitle());
    }

    @Test
    void insertsAndRetrieve() {
        Book saved = repo.save(new Book("Test Book", "Test Author", 2026));
        try {
            Book found = repo.findById(saved.getId()).orElseThrow();
            Assertions.assertEquals("Test Book", found.getTitle());
            Assertions.assertEquals("Test Author", found.getAuthor());
            Assertions.assertEquals(2026, found.getYear());
        } finally {
            // Clean up
            repo.deleteById(saved.getId());
        }
    }
}
