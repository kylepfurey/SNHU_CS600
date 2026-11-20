// .java
// Home View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;

/**
 * Frontend: Home Page
 */
@Route("")
@PageTitle("Sales Guru+")
@PermitAll
public class HomeView extends VerticalLayout {
    /**
     * Constructor
     */
    public HomeView() {
        add(new H1("Sales Guru+"), new Paragraph("Signed in."));
    }
}
