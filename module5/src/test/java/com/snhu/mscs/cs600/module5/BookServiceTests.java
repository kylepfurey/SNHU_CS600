// .java
// Book Service Testing
// by Kyle Furey

package com.snhu.mscs.cs600.module5;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Book Service Tests
 */
public class BookServiceTests {
    @Test
    void testCreate() {
        BookService bookService = new BookService();
        int size = bookService.read().size();
        Book book = new Book(
                "Test Book",
                "Test Author",
                2004
        );
        bookService.create(book);
        Assertions.assertEquals(
                size + 1,
                bookService.read().size()
        );
    }

    @Test
    void testRead() {
        Assertions.assertEquals(
                TestData.book_list.length,
                new BookService().read().size()
        );
    }

    @Test
    void testUpdate() {
        BookService bookService = new BookService();
        Book book = new Book(
                "Bad",
                "Bad Author",
                2003
        );
        bookService.create(book);
        bookService.update(book, "Test", "Test Author", 2004);
        Assertions.assertEquals("Test", book.getTitle());
        Assertions.assertEquals("Test Author", book.getAuthor());
        Assertions.assertEquals(2004, book.getYear());
    }

    @Test
    void testDelete() {
        BookService bookService = new BookService();
        Book book = new Book(
                "Test",
                "Test Author",
                2004
        );
        bookService.create(book);
        bookService.delete(book);
        Assertions.assertFalse(bookService.read().contains(book));
    }
}
