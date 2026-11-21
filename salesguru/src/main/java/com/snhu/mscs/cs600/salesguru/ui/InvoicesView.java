// .java
// Invoices View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.snhu.mscs.cs600.salesguru.api.SalesGuruService;
import com.snhu.mscs.cs600.salesguru.api.InvoiceService;
import com.snhu.mscs.cs600.salesguru.api.ItemService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.snhu.mscs.cs600.salesguru.entities.Company;
import com.vaadin.flow.component.grid.Grid;
import com.snhu.mscs.cs600.salesguru.entities.Invoice;
import com.snhu.mscs.cs600.salesguru.api.InvoiceData;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.List;
import java.util.NoSuchElementException;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.dialog.Dialog;
import java.util.ArrayList;
import com.snhu.mscs.cs600.salesguru.entities.Representative;
import com.vaadin.flow.component.datepicker.DatePicker;
import java.time.LocalDate;
import com.snhu.mscs.cs600.salesguru.entities.Item;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.BigDecimalField;
import java.math.BigDecimal;

/**
 * Frontend: Invoices Table
 */
@Route(value = "invoices", layout = MainLayout.class)
@PageTitle("Invoices | Sales Guru+")
@PermitAll
public class InvoicesView extends VerticalLayout {
    /**
     * Services
     */
    final SalesGuruService salesGuruService;
    final InvoiceService invoiceService;
    final ItemService itemService;

    /**
     * Components
     */
    final Grid<Invoice> invoiceGrid = new Grid<>();
    final Grid<InvoiceData.Line> lineGrid = new Grid<>();
    final H3 defaultHeader = new H3("Select an invoice to see its line items");
    final Paragraph totalParagraph = new Paragraph();
    final ComboBox<Company> filterComboBox = new ComboBox<>("Company");

    /**
     * Constructor
     */
    public InvoicesView(
            SalesGuruService salesGuruService,
            InvoiceService invoiceService,
            ItemService itemService
    ) {
        this.salesGuruService = salesGuruService;
        this.invoiceService = invoiceService;
        this.itemService = itemService;

        // Filter
        filterComboBox.setItems(salesGuruService.listCompanies());
        filterComboBox.setItemLabelGenerator(comboBox -> comboBox.getCustomerNumber() + " - " + comboBox.getName());
        filterComboBox.setClearButtonVisible(true);
        filterComboBox.setWidth("24em");
        filterComboBox.addValueChangeListener(event -> refresh(event.getValue()));

        // Invoice table
        invoiceGrid.addColumn(Invoice::getInvoiceNumber)
                .setHeader("Invoice #")
                .setSortable(true);
        invoiceGrid.addColumn(Invoice::getCustomerNumber)
                .setHeader("Customer #")
                .setSortable(true);
        invoiceGrid.addColumn(Invoice::getRepresentativeId)
                .setHeader("Rep ID")
                .setSortable(true);
        invoiceGrid.addColumn(Invoice::getDateOfSale)
                .setHeader("Date of sale")
                .setSortable(true);
        invoiceGrid.setItems(invoiceService.listInvoices());
        invoiceGrid.asSingleSelect()
                .addValueChangeListener(event -> showInvoiceData(event.getValue()));

        // Line table
        lineGrid.addColumn(InvoiceData.Line::itemNumber)
                .setHeader("Item #");
        lineGrid.addColumn(InvoiceData.Line::description)
                .setHeader("Description");
        lineGrid.addColumn(InvoiceData.Line::qty)
                .setHeader("Qty");
        lineGrid.addColumn(line -> line.unitPrice().toPlainString())
                .setHeader("Unit price");
        lineGrid.addColumn(line -> line.discount().toPlainString())
                .setHeader("Discount");
        lineGrid.addColumn(line -> line.lineTotal().toPlainString())
                .setHeader("Line total");

        // Create invoice
        Button newButton = new Button("New invoice", event -> createInvoice());

        setSizeFull();
        add(
                new H2("Invoices"),
                new HorizontalLayout(
                        filterComboBox,
                        newButton
                ),
                invoiceGrid,
                defaultHeader,
                lineGrid,
                totalParagraph
        );
    }

    /**
     * Helpers
     */
    void showInvoiceData(Invoice invoice) {
        if (invoice == null) {
            defaultHeader.setText("Select an invoice to see its line items");
            lineGrid.setItems(List.of());
            totalParagraph.setText("");
            return;
        }
        try {
            InvoiceData invoiceData = invoiceService.getInvoice(invoice.getInvoiceNumber());
            defaultHeader.setText("Invoice " + invoiceData.invoiceNumber());
            lineGrid.setItems(invoiceData.lines());
            totalParagraph.setText("Total: " + invoiceData.total().toPlainString());
        } catch (NoSuchElementException | IllegalStateException error) {
            Notification.show("Could not load invoice " + invoice.getInvoiceNumber());
        }
    }
    void refresh(Company company) {
        invoiceGrid.setItems(company == null ?
                invoiceService.listInvoices() :
                invoiceService.listInvoicesForCompany(company.getCustomerNumber()));
        showInvoiceData(null);
    }
    void createInvoice() {
        // Popup
        Dialog dialog = new Dialog("New invoice");
        List<InvoiceData.Request> requests = new ArrayList<>();

        // Select company
        ComboBox<Company> companyComboBox = new ComboBox<>("Company");
        companyComboBox.setItems(salesGuruService.listCompanies());
        companyComboBox.setItemLabelGenerator(comboBox -> comboBox.getCustomerNumber() + " - " + comboBox.getName());

        // Select representative
        ComboBox<Representative> representativeComboBox = new ComboBox<>("Representative");
        representativeComboBox.setEnabled(false);
        representativeComboBox.setItemLabelGenerator(representative ->
                representative.getRepresentativeId() + " - " + representative.getFirst() + " " + representative.getLast());
        companyComboBox.addValueChangeListener(event -> {
            representativeComboBox.clear();
            representativeComboBox.setEnabled(event.getValue() != null);
            representativeComboBox.setItems(event.getValue() == null ?
                    List.of() :
                    salesGuruService.listRepresentativesForCompany(event.getValue().getCustomerNumber())
            );
        });

        // Select date
        DatePicker datePicker = new DatePicker("Date of sale", LocalDate.now());

        // Select item
        ComboBox<Item> itemComboBox = new ComboBox<>("Item");
        itemComboBox.setItems(itemService.listItems());
        itemComboBox.setItemLabelGenerator(item -> item.getItemNumber() + " - " + item.getDescription());

        // Enter quantity
        IntegerField qtyField = new IntegerField("Qty");
        qtyField.setValue(1);

        // Enter decimal
        BigDecimalField discountField = new BigDecimalField("Discount (0 to 1)");
        discountField.setValue(BigDecimal.ZERO);

        // List requests
        Grid<InvoiceData.Request> requestGrid = new Grid<>();
        requestGrid.addColumn(InvoiceData.Request::itemNumber)
                .setHeader("Item #");
        requestGrid.addColumn(InvoiceData.Request::qty)
                .setHeader("Qty");
        requestGrid.addColumn(request -> request.discount().toPlainString()).setHeader("Discount");
        requestGrid.setAllRowsVisible(true);

        // Append line
        Button addLineButton = new Button("Add line", event -> {
            if (itemComboBox.getValue() == null || qtyField.getValue() == null) {
                Notification.show("Choose an item and a quantity");
                return;
            }
            BigDecimal discount = discountField.getValue() == null ? BigDecimal.ZERO : discountField.getValue();
            requests.add(new InvoiceData.Request(itemComboBox.getValue().getItemNumber(), qtyField.getValue(), discount));
            requestGrid.setItems(List.copyOf(requests));
        });

        // Pop line
        Button removeLineButton = new Button("Remove last", event -> {
            if (!requests.isEmpty()) {
                requests.remove(requests.size() - 1);
                requestGrid.setItems(List.copyOf(requests));
            }
        });

        // Save button
        Button saveButton = new Button("Save", event -> {
            if (companyComboBox.getValue() == null || representativeComboBox.getValue() == null || datePicker.getValue() == null) {
                Notification.show("Company, representative, and date are required");
                return;
            }
            try {
                InvoiceData created = invoiceService.createInvoice(
                        companyComboBox.getValue().getCustomerNumber(),
                        representativeComboBox.getValue().getRepresentativeId(),
                        datePicker.getValue(),
                        List.copyOf(requests)
                );
                Notification.show("Created invoice " + created.invoiceNumber());
                dialog.close();
                filterComboBox.clear();
                refresh(null);
            } catch (IllegalArgumentException | NoSuchElementException error) {
                Notification.show(error.getMessage());
            }
        });

        // Cancel button
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(
                new VerticalLayout(
                        companyComboBox,
                        representativeComboBox,
                        datePicker,
                        new HorizontalLayout(itemComboBox, qtyField, discountField),
                        new HorizontalLayout(addLineButton, removeLineButton),
                        requestGrid
                )
        );

        dialog.getFooter().add(cancelButton, saveButton);
        dialog.open();
    }
}
