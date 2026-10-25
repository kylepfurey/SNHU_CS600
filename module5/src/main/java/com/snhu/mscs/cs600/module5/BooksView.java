// .java
// Books View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.module5;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
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
        Grid.Column<Book> titles = grid.addColumn(Book::getTitle)
                .setHeader("Title")
                .setSortable(true)
                .setFlexGrow(3);
        Grid.Column<Book> authors = grid.addColumn(Book::getAuthor)
                .setHeader("Author")
                .setSortable(true)
                .setFlexGrow(2);
        Grid.Column<Book> years = grid.addColumn(Book::getYear)
                .setSortable(true)
                .setHeader("Year");
        grid.sort(GridSortOrder.asc(titles).build());

        // 11. Read-Only Grid
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // 8. TestData in Grid
        GridListDataView<Book> dataView = grid.setItems(book_list);

        // Search Bar
        TextField searchBar = new TextField();
        searchBar.setPlaceholder("Search title, author, or year");
        searchBar.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchBar.setClearButtonVisible(true);
        searchBar.setWidth("30em");
        searchBar.setValueChangeMode(ValueChangeMode.LAZY);
        searchBar.addValueChangeListener(e -> {
            String query = e.getValue().trim().toLowerCase();
            dataView.setFilter(book ->
                    query.isEmpty()
                            || book.getTitle().toLowerCase().contains(query)
                            || book.getAuthor().toLowerCase().contains(query)
                            || String.valueOf(book.getYear()).contains(query)
            );
        });

        grid.setSizeFull();
        setSizeFull();
        add(new H2("Books List"), searchBar, grid);
    }
}
