// .java
// Books Service Testing
// by Kyle Furey

package com.snhu.mscs.cs600.module5;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Books Service Tests
 */
public class BooksServiceTests {
    @Test
    void testCreate() {
        BooksService booksService = new BooksService();
        int size = booksService.read().size();
        Book book = new Book(
                "Test Book",
                "Test Author",
                2004
        );
        booksService.create(book);
        Assertions.assertEquals(
                size + 1,
                booksService.read().size()
        );
    }

    @Test
    void testRead() {
        Assertions.assertEquals(
                TestData.book_list.length,
                new BooksService().read().size()
        );
    }

    @Test
    void testUpdate() {
        BooksService booksService = new BooksService();
        Book book = new Book(
                "Bad",
                "Bad Author",
                2003
        );
        booksService.create(book);
        booksService.update(book, "Test", "Test Author", 2004);
        Assertions.assertEquals("Test", book.getTitle());
        Assertions.assertEquals("Test Author", book.getAuthor());
        Assertions.assertEquals(2004, book.getYear());
    }

    @Test
    void testDelete() {
        BooksService booksService = new BooksService();
        Book book = new Book(
                "Test",
                "Test Author",
                2004
        );
        booksService.create(book);
        booksService.delete(book);
        Assertions.assertFalse(booksService.read().contains(book));
    }
}
