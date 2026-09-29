package com.snhu.mscs.cs600.base.ui;

/**
 * Imports for Vaadin UI components.
 */
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Custom Security Service for this application.
 */
import com.snhu.mscs.cs600.base.ui.component.SecurityService;

/**
 * Import a single copy of the theme elements to be reused across the
 * application.
 */
import static com.vaadin.flow.theme.lumo.LumoUtility.*;

/**
 * MainLayout - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the structure for the
 *      application. This defines the Layout of how the application will
 *      appear to the user. In this case we are providing a side-by-side
 *      layout with navigation on the left side of the page and data 
 *      displayed on the right side of the page.
 * 
 * Extends: 
 *      AppLayout - The basic Application Layout class that provides
 *      foundational capabilities. All Vaadin Layout classes should 
 *      descend from this class.
 *
 * Class Level Annotations:
 *      @Layout - This tells the Vaadin framework that this class is a
 *      Layout class. This is necessary for loading purposes when building
 *      the UI. 
 *      @AnonymousAllowed - This annotation is required for the primary 
 *      Layout for the application when you are impementing Spring Security.
 *      without it, the application will not function properly even when the
 *      user authenticates because of the order in which the processing
 *      pipeline builds the UI.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class will be used as the Layout for subsequent display of
 *      Views for this application.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        20-Oct-2025     Initial Creation
 */
@Layout
@AnonymousAllowed
public final class MainLayout extends AppLayout {

    /**
     * securityService - Service used to access information relevant to the
     * security configuration for this application.
     */
    @Autowired
    private SecurityService securityService;

    /**
     * MainLayout - Default constructor for the layout. Actions taken here
     * are to create a 'Drawer' construct to hold UI components. In this case, 
     * including a header and a scrollable side-navigation panel.
     * 
     * @param securityService SecurityService object used to access and manipulate
     *          the security parameters of our application.
     */
    MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        setPrimarySection(Section.DRAWER);
        addToDrawer(createHeader(), new Scroller(createSideNav()));
    }

    /**
     * createHeader - Helper method used to configure the header for our 
     * application. This will include a graphical logo for the application, 
     * and the application name.
     * 
     * @return an HTML Div object containing the necessary UI elements 
     *         for display.
     */
    private Div createHeader() {
        // This can be replaced with a real logo for production applications.
        var appLogo = VaadinIcon.CUBES.create();
        appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE);
    
        // Configure the Name of the application to display in the header
        var appName = new Span("Module7");
        appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

        // HTML Construct to display Logo and Name
        var header = new Div();

        // If the user has been authenticated, add a logout button. 
        if(securityService.getAuthenticatedUser() != null)
        {
            Button logout = new Button("Logout", click -> 
                securityService.logout());
            header = new Div(appLogo, appName, logout);
        }
        else
        {
            header = new Div(appLogo, appName);
        }

        // Configure how the header is going to be displayed and return the header object
        header.addClassNames(Display.FLEX, Padding.MEDIUM, Gap.MEDIUM, AlignItems.CENTER);
        return header;
    }

    /**
     * createSideNav - Helper method used to create and configure the side-navigation pane
     * and setup the menu.
     * 
     * @return SideNav object with configured navigation menu.
     */
    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(Margin.Horizontal.MEDIUM);

        /**
         * Loop through the application and pull menu entries for each annotated View class.
         * The menu entries will be ordered based on their annotated values.
         */
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    /**
     * createSideNavItem - helper method used to create and style each item in the menu. This
     * takes input from the @menu annotation for each View class that will be represented in the
     * menu.
     * 
     * @param menuEntry an entry generated from the @menu annotations for View objects.
     * @return an individual SideNavItem used to create the navigation menu.
     */
    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }
}
