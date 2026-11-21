// .java
// Home View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.snhu.mscs.cs600.salesguru.api.SalesGuruService;
import com.snhu.mscs.cs600.salesguru.api.ItemService;
import com.snhu.mscs.cs600.salesguru.api.InvoiceService;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import java.util.List;
import com.snhu.mscs.cs600.salesguru.entities.Invoice;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import java.util.Comparator;

/**
 * Frontend: Home Page
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Sales Guru+")
@PermitAll
public class HomeView extends VerticalLayout {
    /**
     * Constants
     */
    static final int RECENT_INVOICES = 5;

    /**
     * Constructor
     */
    public HomeView(
            SalesGuruService salesGuruService,
            ItemService itemService,
            InvoiceService invoiceService,
            AuthenticationContext authenticationContext
    ) {
        // Greeting
        String username = authenticationContext.getPrincipalName()
                .orElse("user");
        H1 titleHeader = new H1("Welcome back, " + username);
        titleHeader.getStyle()
                .set("margin", "0")
                .set("color", "var(--lumo-primary-text-color)");
        Paragraph subtitleParagraph = new Paragraph("Thanks for choosing Sales Guru+");
        subtitleParagraph.getStyle()
                .set("margin", "0")
                .set("color", "var(--lumo-secondary-text-color)");

        // Statistics
        List<Invoice> invoices = invoiceService.listInvoices();
        FlexLayout cardsLayout = new FlexLayout(
                statistic("Companies", salesGuruService.listCompanies().size(), VaadinIcon.BUILDING, CompaniesView.class),
                statistic("Representatives", salesGuruService.listRepresentatives().size(), VaadinIcon.USERS, RepresentativesView.class),
                statistic("Items", itemService.listItems().size(), VaadinIcon.PACKAGE, ItemsView.class),
                statistic("Invoices", invoices.size(), VaadinIcon.INVOICE, InvoicesView.class)
        );
        cardsLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        cardsLayout.getStyle().set("gap", "var(--lumo-space-l)");

        // Recent invoices
        H3 recentHeader = new H3("Recent invoices");
        recentHeader.getStyle().set("margin", "0");
        Component recentComponent = invoices.isEmpty() ?
                new Paragraph("No invoices yet.") :
                recentInvoices(invoices);

        setPadding(true);
        setSpacing(true);
        add(titleHeader, subtitleParagraph, cardsLayout, recentHeader, recentComponent);
    }

    /**
     * Helpers
     */
    static Div statistic(String label, int value, VaadinIcon icon, Class<? extends Component> target) {
        // Icon
        Icon cardIcon = icon.create();
        cardIcon.setSize("var(--lumo-icon-size-l)");
        cardIcon.setColor("var(--lumo-primary-color)");

        // Number
        Span numberSpan = new Span(String.valueOf(value));
        numberSpan.getStyle()
                .set("font-size", "var(--lumo-font-size-xxxl)")
                .set("font-weight", "700")
                .set("line-height", "1");

        // Name
        Span nameSpan = new Span(label);
        nameSpan.getStyle().set("color", "var(--lumo-secondary-text-color)");

        // Object
        Div textDiv = new Div(numberSpan, new Div(nameSpan));
        Div cardDiv = new Div(cardIcon, textDiv);
        cardDiv.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "var(--lumo-space-m)")
                .set("min-width", "13em")
                .set("padding", "var(--lumo-space-l)")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("border", "1px solid var(--lumo-contrast-10pct)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("cursor", "pointer");
        cardDiv.getElement().addEventListener("mouseenter", event ->
                cardDiv.getStyle().set("border-color", "var(--lumo-primary-color)"));
        cardDiv.getElement().addEventListener("mouseleave", event ->
                cardDiv.getStyle().set("border-color", "var(--lumo-contrast-10pct)"));
        cardDiv.addClickListener(event -> UI.getCurrent().navigate(target));

        return cardDiv;
    }
    static Grid<Invoice> recentInvoices(List<Invoice> invoices) {
        Grid<Invoice> grid = new Grid<>();
        grid.addColumn(Invoice::getInvoiceNumber).setHeader("Invoice #");
        grid.addColumn(Invoice::getCustomerNumber).setHeader("Customer #");
        grid.addColumn(Invoice::getRepresentativeId).setHeader("Rep ID");
        grid.addColumn(Invoice::getDateOfSale).setHeader("Date of sale");
        grid.setItems(
                invoices.stream()
                        .sorted(Comparator.comparing(Invoice::getInvoiceNumber).reversed())
                        .limit(RECENT_INVOICES)
                        .toList()
        );
        grid.setAllRowsVisible(true);
        return grid;
    }
}
