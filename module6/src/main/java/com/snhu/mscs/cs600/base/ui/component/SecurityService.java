package com.snhu.mscs.cs600.base.ui.component;

/**
 * Imports to support integration with Spring Security
 */
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import com.snhu.mscs.cs600.Application;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

/**
 * SecurityService - 
 * 
 * Purpose:
 *      The purpose of this class is to support interaction with authenticated
 *      users. This allows the program to access the UserDetails of for a user
 *      in a given session and pull the necessary information to support action
 *      within the application. It also provides a method to log the user out
 *      of the system.
 * 
 * Extends: 
 *      N/A
 *
 * Class Level Annotations:
 *      @Component - Annotation that identifies this as a Component to the
 *      Spring Framework. While this class does not provide any beans, this
 *      annotation enables support for Spring to be able to auto-wire this
 *      service where necessary to enable directed use of the class.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class will be used as a utility class to interact with various 
 *      security objects within the application.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        20-Oct-2025     Initial Creation
 */
@Component
public class SecurityService {

    /**
     * This is the application for which this service is being defined.
     */
    private final Application application;

    /**
     * LOGOUT_SUCCESS_URL - the landing page (Route path) to revert to 
     * upon successful logout.
     */
    private static final String LOGOUT_SUCCESS_URL = "/";

    /**
     SecurityService -
     * Default constructor for this class. Sets the application internally.
     * 
     * @param application The application instance that we have authenticated against.
     */
    SecurityService(Application application) {
        this.application = application;
    }

    /**
     * getAuthenticatedUser - method used to retrieve information on the user
     * logged into the current session.
     * 
     * @return UserDetails object representing the user for the current session.
     */
    public UserDetails getAuthenticatedUser() {

        // Get the principal for the current session. If the principal 
        // is an instance of UserDetails, return it.
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        // Anonymous or no authentication.
        return null;
    }

    /**
     * logout - Method to invalidate the current user session.
     */
    public void logout() {
        // Set the current page to the default starting page.
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);

        // Handle the logout
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
}