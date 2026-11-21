// .java
// Invoices View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.snhu.mscs.cs600.salesguru.api.InvoiceService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.H2;
import com.snhu.mscs.cs600.salesguru.entities.Invoice;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.api.InvoiceData;
import java.util.NoSuchElementException;
import com.vaadin.flow.component.notification.Notification;

/**
 * Frontend: Invoices Table
 */
@Route(value = "invoices", layout = MainLayout.class)
@PageTitle("Invoices | Sales Guru+")
@PermitAll
public class InvoicesView extends VerticalLayout {
    /**
     * Service
     */
    final InvoiceService invoiceService;

    /**
     * Components
     */
    final Grid<Invoice> invoiceGrid = new Grid<>();
    final Grid<InvoiceData.Line> lineGrid = new Grid<>();
    final H3 defaultHeader = new H3("Select an invoice to see its line items");
    final Paragraph totalText = new Paragraph();

    /**
     * Constructor
     */
    public InvoicesView(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;

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
                .addValueChangeListener(event -> showDetail(event.getValue()));

        // Line table
        lineGrid.addColumn(InvoiceData.Line::itemNumber)
                .setHeader("Item #");
        lineGrid.addColumn(InvoiceData.Line::description)
                .setHeader("Description");
        lineGrid.addColumn(InvoiceData.Line::qty)
                .setHeader("Qty");
        lineGrid.addColumn(l -> l.unitPrice().toPlainString())
                .setHeader("Unit price");
        lineGrid.addColumn(l -> l.discount().toPlainString())
                .setHeader("Discount");
        lineGrid.addColumn(l -> l.lineTotal().toPlainString())
                .setHeader("Line total");

        setSizeFull();
        add(new H2("Invoices"), invoiceGrid, defaultHeader, lineGrid, totalText);
    }

    /**
     * Helpers
     */
    void showDetail(Invoice invoice) {
        if (invoice == null) {
            defaultHeader.setText("Select an invoice to see its line items");
            lineGrid.setItems(List.of());
            totalText.setText("");
            return;
        }
        try {
            InvoiceData invoiceData = invoiceService.getInvoice(invoice.getInvoiceNumber());
            defaultHeader.setText("Invoice " + invoiceData.invoiceNumber());
            lineGrid.setItems(invoiceData.lines());
            totalText.setText("Total: " + invoiceData.total().toPlainString());
        } catch (NoSuchElementException | IllegalStateException error) {
            Notification.show("Could not load invoice " + invoice.getInvoiceNumber());
        }
    }
}
