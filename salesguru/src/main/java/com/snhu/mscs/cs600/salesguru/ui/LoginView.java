// .java
// Login View Frontend
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEnterEvent;

/**
 * Frontend: Login Page
 */
@Route("login")
@PageTitle("Login | Sales Guru+")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    /**
     * Forms
     */
    final LoginForm loginForm = new LoginForm();

    /**
     * Constructor
     */
    public LoginView() {
        // Login
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);

        add(new H1("Sales Guru+"), loginForm);
    }

    /**
     * Events
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Login error
        loginForm.setError(
                event.getLocation()
                        .getQueryParameters()
                        .getParameters()
                        .containsKey("error")
        );
    }
}
