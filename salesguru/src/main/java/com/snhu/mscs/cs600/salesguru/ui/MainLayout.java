// .java
// Main Layout Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.applayout.DrawerToggle;

/**
 * Frontend: Main Layout
 */
public class MainLayout extends AppLayout {
    /**
     * Constructor
     */
    public MainLayout(AuthenticationContext authenticationContext) {
        // Title header
        H1 titleHeader = new H1("Sales Guru+");
        titleHeader.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        // Logout
        Button logoutButton = new Button("Log out", event -> authenticationContext.logout());

        // Side navigation
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Home", HomeView.class, VaadinIcon.HOME.create()),
                new SideNavItem("Companies", CompaniesView.class, VaadinIcon.BUILDING.create()),
                new SideNavItem("Representatives", RepresentativesView.class, VaadinIcon.USERS.create()),
                new SideNavItem("Items", ItemsView.class, VaadinIcon.PACKAGE.create()),
                new SideNavItem("Invoices", InvoicesView.class, VaadinIcon.INVOICE.create())
        );

        addToDrawer(sideNav);
        addToNavbar(new DrawerToggle(), titleHeader, logoutButton);
    }
}
