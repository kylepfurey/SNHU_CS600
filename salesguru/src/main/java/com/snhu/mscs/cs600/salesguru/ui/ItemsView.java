// .java
// Items View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.snhu.mscs.cs600.salesguru.api.ItemService;
import com.vaadin.flow.component.grid.Grid;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

/**
 * Frontend: Items Table
 */
@Route(value = "items", layout = MainLayout.class)
@PageTitle("Items | Sales Guru+")
@PermitAll
public class ItemsView extends VerticalLayout {
    /**
     * Service
     */
    final ItemService itemService;

    /**
     * Components
     */
    final Grid<Item> itemsGrid = new Grid<>();
    final TextField descriptionTextField = new TextField("Description");
    final BigDecimalField costField = new BigDecimalField("Cost");
    final BigDecimalField retailField = new BigDecimalField("Retail");

    /**
     * Constructor
     */
    public ItemsView(ItemService itemService) {
        this.itemService = itemService;

        // Items table
        itemsGrid.addColumn(Item::getItemNumber)
                .setHeader("Item #")
                .setSortable(true);
        itemsGrid.addColumn(Item::getDescription)
                .setHeader("Description")
                .setSortable(true);
        itemsGrid.addColumn(Item::getCost)
                .setHeader("Cost")
                .setSortable(true);
        itemsGrid.addColumn(Item::getRetail)
                .setHeader("Retail")
                .setSortable(true);

        // Entered description
        descriptionTextField.setMaxLength(ItemService.DESCRIPTION_LENGTH);
        descriptionTextField.setWidth("22em");

        // Add button
        Button addButton = new Button("Add item", event -> save());

        // Horizontal layout
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                descriptionTextField,
                costField,
                retailField,
                addButton
        );
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        setSizeFull();
        add(new H2("Items"), horizontalLayout, itemsGrid);
        refresh();
    }

    /**
     * Helpers
     */
    void save() {
        try {
            itemService.createItem(
                    descriptionTextField.getValue(),
                    costField.getValue(),
                    retailField.getValue()
            );
            descriptionTextField.clear();
            costField.clear();
            retailField.clear();
            refresh();
        } catch (IllegalArgumentException error) {
            Notification.show(error.getMessage());
        }
    }
    void refresh() {
        itemsGrid.setItems(itemService.listItems());
    }
}
