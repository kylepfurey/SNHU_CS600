// .java
// Books Layout Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.module5;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;

/**
 * Frontend: Books Layout
 */
public class BooksLayout extends AppLayout {
    /**
     * Constructor
     */
    public BooksLayout() {
        // 2. Drawer
        setPrimarySection(Section.DRAWER);
        addToDrawer(
                createHeader(),
                // 10. Scrollbar
                new Scroller(createSideNav())
        );
    }

    /**
     * 1. Side-Navigation Panel
     */
    SideNav createSideNav() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem(
                        "Books List",
                        BooksView.class,
                        VaadinIcon.BOOK.create()
                )
        );
        return sideNav;
    }

    /**
     * 3. Header
     */
    Div createHeader() {
        // 3.1 Logo
        Image logo = new Image("images/logo.png", "Logo");
        logo.setHeight("32px");

        // 3.2 Application Name
        Span appName = new Span("Module5");
        appName.getStyle().set("font-weight", "bold");

        // 3.3 My Name
        Span author = new Span("Kyle Furey");

        Div header = new Div(logo, appName, author);
        header.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "0.75rem")
                .set("padding", "0.75rem");
        return header;
    }
}
