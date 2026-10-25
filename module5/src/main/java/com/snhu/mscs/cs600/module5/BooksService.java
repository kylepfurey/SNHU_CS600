// .java
// Books Service Backend
// by Kyle Furey

package com.snhu.mscs.cs600.module5;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Backend: Books Service
 */
@Service
public class BooksService {
    /**
     * Thread-Safe Array List
     */
    final List<Book> books = new CopyOnWriteArrayList<Book>(
            Arrays.asList(TestData.book_list)
    );

    /**
     * Create
     */
    public void create(Book book) { books.add(book); }

    /**
     * Read
     */
    public List<Book> read() { return new ArrayList<Book>(books); }

    /**
     * Update
     */
    public void update(Book book, String title, String author, int year) {
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
    }

    /**
     * Delete
     */
    public void delete(Book book) { books.remove(book); }
}
