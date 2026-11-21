// .java
// Companies View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.snhu.mscs.cs600.salesguru.api.SalesGuruService;
import com.vaadin.flow.component.grid.Grid;
import com.snhu.mscs.cs600.salesguru.entities.Company;
import com.vaadin.flow.component.html.H2;

/**
 * Frontend: Companies Table
 */
@Route(value = "companies", layout = MainLayout.class)
@PageTitle("Companies | Sales Guru+")
@PermitAll
public class CompaniesView extends VerticalLayout {
    /**
     * Constructor
     */
    public CompaniesView(SalesGuruService service) {
        // Companies table
        Grid<Company> companiesGrid = new Grid<>();
        companiesGrid.addColumn(Company::getCustomerNumber)
                .setHeader("Customer #")
                .setSortable(true);
        companiesGrid.addColumn(Company::getName)
                .setHeader("Name")
                .setSortable(true);
        companiesGrid.addColumn(Company::getAddr1).
                setHeader("Address 1");
        companiesGrid.addColumn(Company::getAddr2)
                .setHeader("Address 2");
        companiesGrid.addColumn(Company::getCity)
                .setHeader("City")
                .setSortable(true);
        companiesGrid.addColumn(Company::getState)
                .setHeader("State")
                .setSortable(true);
        companiesGrid.addColumn(Company::getZip)
                .setHeader("ZIP");
        companiesGrid.setItems(service.listCompanies());

        setSizeFull();
        add(new H2("Companies"), companiesGrid);
    }
}
