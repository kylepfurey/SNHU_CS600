// .java
// Representatives View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.snhu.mscs.cs600.salesguru.api.SalesGuruService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.combobox.ComboBox;
import com.snhu.mscs.cs600.salesguru.entities.Company;
import com.snhu.mscs.cs600.salesguru.entities.Representative;
import com.vaadin.flow.component.html.H2;
import java.util.stream.Stream;

/**
 * Frontend: Representatives Table
 */
@Route(value = "representatives", layout = MainLayout.class)
@PageTitle("Representatives | Sales Guru+")
@PermitAll
public class RepresentativesView extends VerticalLayout {
    /**
     * Service
     */
    final SalesGuruService salesGuruService;

    /**
     * Components
     */
    final Grid<Representative> representativesGrid = new Grid<>();

    /**
     * Constructor
     */
    public RepresentativesView(SalesGuruService salesGuruService) {
        this.salesGuruService = salesGuruService;

        // Filter
        ComboBox<Company> filterComboBox = new ComboBox<>("Company");
        filterComboBox.setItems(salesGuruService.listCompanies());
        filterComboBox.setItemLabelGenerator(c -> c.getCustomerNumber() + " - " + c.getName());
        filterComboBox.setClearButtonVisible(true);
        filterComboBox.setWidth("24em");
        filterComboBox.addValueChangeListener(event -> refresh(event.getValue()));

        // Representatives table
        representativesGrid.addColumn(Representative::getRepresentativeId)
                .setHeader("Rep ID")
                .setSortable(true);
        representativesGrid.addColumn(Representative::getCustomerNumber)
                .setHeader("Customer #")
                .setSortable(true);
        representativesGrid.addColumn(RepresentativesView::fullName)
                .setHeader("Name")
                .setSortable(true);
        representativesGrid.addColumn(Representative::getDirectLine)
                .setHeader("Direct line");
        representativesGrid.addColumn(Representative::getMobile)
                .setHeader("Mobile");

        setSizeFull();
        add(new H2("Representatives"), filterComboBox, representativesGrid);
        refresh(null);
    }

    /**
     * Helpers
     */
    void refresh(Company company) {
        representativesGrid.setItems(company == null
                ? salesGuruService.listRepresentatives()
                : salesGuruService.listRepresentativesForCompany(company.getCustomerNumber()));
    }
    static String fullName(Representative representative) {
        return String.join(
                " ",
                Stream.of(
                        representative.getSalutation(),
                        representative.getFirst(),
                        representative.getMiddle(),
                        representative.getLast(),
                        representative.getSuffix()
                )
                .filter(str -> str != null && !str.isBlank())
                .toList()
        );
    }
}
