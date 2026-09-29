package com.snhu.mscs.cs600.base.ui;

/**
 * Imports representing Vaadin UI and Login related components.
 */
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * LoginView - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the LoginForm and logic
 *      necessary to authenticate a user to the application. This leverages
 *      functionality built into Vaadin and Spring Security to reduce the 
 *      complexity of the Security Configuration.
 * 
 * Extends: 
 *      VeritcalLayout - A basic layout structure where items are added to
 *      the canvas in a vertical orientation.
 * 
 * Implements: 
 *      BeforeEnterObserver - This provides an onBeforeEnter method that
 *      gets executed prior to other methods in the route tree. 
 *      
 *
 * Class Level Annotations:
 *      @Route - This tells the Vaadin framework what the URL path is 
 *      relative to our base URL. "" is the default route for the 
 *      application.
 *      @PageTitle - This tells the Vaadin framework what the page is
 *      labeled.
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
 *      This class configures application security.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        20-Oct-2025     Initial Creation
 */
@Route(value = "login")
@PageTitle("Login")
@AnonymousAllowed
@Menu(order = 2, icon = "vaadin:clipboard-check", title = "Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    /**
     * login - Vaadin LoginForm resource to provide basic Login Form for
     * authentication.
     */
    private LoginForm login = new LoginForm();


    /**
     * LoginView - Simple constructor to setup the login page for Authentication.
     */
    public LoginView() {
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");

        add(new H1("Module 5 Lab"), login);
    }

    /**
     * beforeEnter - implemented as part of the BeforeEnterObserver interface,
     * this method may be executed prior to other steps in the Routing process.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
            login.setError(true);
        }
    } 
}