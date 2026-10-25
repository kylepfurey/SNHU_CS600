// .java
// Books View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.module5;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;

/**
 * 4. Frontend: Books View
 */
@Route(value = "", layout = BooksLayout.class)
@PageTitle("Books List") // 5. Books List
public class BooksView extends VerticalLayout implements TestData { // 7. TestData Interface
    /**
     * Constructor
     */
    public BooksView() {
        // 6. Grid Component
        Grid<Book> grid = new Grid<Book>(Book.class, false);

        // 9. Grid Columns
        grid.addColumn(Book::getTitle)
                .setHeader("Title")
                .setSortable(true)
                .setFlexGrow(3);
        grid.addColumn(Book::getAuthor)
                .setHeader("Author")
                .setSortable(true)
                .setFlexGrow(2);
        grid.addColumn(Book::getYear)
                .setSortable(true)
                .setHeader("Year");

        // 11. Read-Only Grid
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // 8. TestData in Grid
        grid.setItems(book_list);

        grid.setSizeFull();
        setSizeFull();
        add(new H2("Books List"), grid);
    }
}
