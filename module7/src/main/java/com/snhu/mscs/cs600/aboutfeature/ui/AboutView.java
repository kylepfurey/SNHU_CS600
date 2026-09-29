package com.snhu.mscs.cs600.aboutfeature.ui;

/**
 * Imports that support the Vaadin flow UI.
 */
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Div;

/**
 * The toolbar we have created for this application
 */
import com.snhu.mscs.cs600.base.ui.component.ViewToolbar;

/**
 * Annotation that allows this view to be used without being authenticated.
 */
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * AboutView - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the UI view for a simple
 *      page that describes the purpose of this application. This is a very
 *      basic implementation of a view that has a single component 
 *      (an HTML Div object) styled to contain HTML formatted text.
 * 
 * Extends: 
 *      VeritcalLayout - A basic layout structure where items are added to
 *      the canvas in a vertical orientation.
 *
 * Class Level Annotations:
 *      @Route - This tells the Vaadin framework what the URL path is 
 *      relative to our base URL. "" is the default route for the 
 *      application.
 *      @AnonymousAllowed - This tells Vaadin and Spring Security that 
 *      this page can be displayed without the user having logged in.
 *      @Menu - This indicates to the Vaadin framework that the view 
 *      should be represented in any menu component for the application
 *      and provides the parameters to order, display, and name the 
 *      menu item.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class will be used as a landing page for the UI.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        20-Oct-2025     Initial Creation
 */
@Route("")
@AnonymousAllowed
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "About")
public class AboutView extends VerticalLayout {

    /**
     * AboutView - As this is a very simple page for our application to display
     *             as a default, all of the configuration is handled in the 
     *             default constructor.
     */
    public AboutView() {
    
        StringBuilder text = new StringBuilder();
        text.append("<P>This is an example of a UI built with the Vaadin Framework.</P>\n");
        text.append("<P>This builds on the Lab from Module 3, utilizing the same concept ");
        text.append("of a list of names stored with Spring JPA in a PostgreSQL database.");
        text.append("<BR/><B><U>Notice:</B></U> In order to see or select the Names View, ");
        text.append("you must authenticate.</P>\n");

        Div content = new Div();
        content.getStyle().set("display", "block").set("white-space", "pre-wrap");
        content.getElement().setProperty("innerHTML", text.toString());
        
        add(new ViewToolbar("About"));
        add(content);
    }
}

