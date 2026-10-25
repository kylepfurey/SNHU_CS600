// .java
// Book View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.module5;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/** Components */
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;

/**
 * Frontend: Book View
 */
@Route("")
@PageTitle("My Books")
public class BookView extends VerticalLayout {
    /**
     * Components
     */
    final BookService bookService;
    final Grid<Book> grid = new Grid<Book>(Book.class, false);
    final TextField title = new TextField("Title");
    final TextField author = new TextField("Author");
    final IntegerField year = new IntegerField("Year");

    /**
     * Constructor
     */
    public BookView(BookService bookService) {
        this.bookService = bookService;

        // Grid
        grid.addColumn(Book::getTitle)
                .setHeader("Title")
                .setSortable(true)
                .setFlexGrow(3);
        grid.addColumn(Book::getAuthor)
                .setHeader("Author")
                .setSortable(true)
                .setFlexGrow(2);
        grid.addColumn(Book::getYear)
                .setHeader("Year")
                .setSortable(true);
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(e -> {
            Book selected = e.getValue();
            if (selected != null) {
                title.setValue(selected.getTitle());
                author.setValue(selected.getAuthor());
                year.setValue(selected.getYear());
            }
        });

        // Buttons
        Button createButton = new Button(
                "Create",
                e -> create()
        );
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button updateButton = new Button(
                "Update",
                e -> update()
        );
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button deleteButton = new Button(
                "Delete",
                e -> delete()
        );
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button clearButton = new Button(
                "Clear",
                e -> clear()
        );
        clearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Layout
        HorizontalLayout form = new HorizontalLayout(
                title,
                author,
                year
        );
        HorizontalLayout buttons = new HorizontalLayout(
                createButton,
                updateButton,
                deleteButton,
                clearButton
        );

        setSizeFull();
        add(new H2("Book List"), form, buttons, grid);
        refresh();
    }

    /**
     * Lifecycle
     */
    void refresh() { grid.setItems(bookService.read()); }
    void clear() {
        title.clear();
        author.clear();
        year.clear();
        grid.asSingleSelect().clear();
    }
    boolean isValid() {
        if (title.getValue().isBlank() || author.getValue().isBlank() || year.isEmpty()) {
            Notification.show("Title, author and year are all required.");
            return false;
        }
        return true;
    }

    /**
     * Create
     */
    void create() {
        if (!isValid()) {
            return;
        }
        bookService.create(
                new Book(
                        title.getValue().trim(),
                        author.getValue().trim(),
                        year.getValue()
                )
        );
        refresh();
        clear();
        Notification.show("Book created.");
    }

    /**
     * Update
     */
    void update() {
        Book selected = grid.asSingleSelect().getValue();
        if (selected == null) {
            Notification.show("Please select a book.");
            return;
        }
        if (!isValid()) {
            return;
        }
        bookService.update(
                selected,
                title.getValue().trim(),
                author.getValue().trim(),
                year.getValue()
        );
        refresh();
        clear();
        Notification.show("Book updated.");
    }

    /**
     * Delete
     */
    void delete() {
        Book selected = grid.asSingleSelect().getValue();
        if (selected == null) {
            Notification.show("Please select a book.");
            return;
        }
        bookService.delete(selected);
        refresh();
        clear();
        Notification.show("Book deleted.");
    }
}
