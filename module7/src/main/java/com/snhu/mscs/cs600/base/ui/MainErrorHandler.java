package com.snhu.mscs.cs600.base.ui;

/**
 * Imports for Vaadin UI to facilitate error messages
 */
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MainErrorHandler - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the primary error handler
 *      for the UI. This will be invoked in the event that the application
 *      UI throws an error to communicate the message back to the user 
 *      through the logging facility.
 * 
 * Extends: 
 *      N/A
 *
 * Class Level Annotations:
 *      @Configuration - Annotation indicating that this class is a source
 *      of Bean definitions. Because this class defines the 
 *      errorHandlerInitializer as a bean, the @Component annotation is 
 *      sufficient to register the listner in Spring Boot.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class will be used for error management within the UI.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        20-Oct-2025     Initial Creation
 */
@Configuration
class MainErrorHandler {

    /**
     * This is the error logging facility. In this case using slf4j (Simple Logging
     * facility for Java). This can be replaced as necessary based on application
     * or business requirements.
     */
    private static final Logger log = LoggerFactory.getLogger(MainErrorHandler.class);


    /**
     * errorHandlerInitializer - Method used to register a SessionInitListener
     * each time a session is initiated with the application. Defining this with
     * the @Bean annotation allows SpringBoot to automatically perform the registration
     * when the application starts.
     * 
     * @return VaadinServiceInitListener object that registers that adds a 
     * new Event listener when a session is initialized with the application.
     */
    @Bean
    VaadinServiceInitListener errorHandlerInitializer() {

        // Use lambda to setup the event listner to manage error handling for
        // sessions.
        return (event) -> event.getSource().addSessionInitListener(
                sessionInitEvent -> sessionInitEvent.getSession().setErrorHandler(errorEvent -> {
                    log.error("An unexpected error occurred", errorEvent.getThrowable());
                    errorEvent.getComponent().flatMap(Component::getUI).ifPresent(ui -> {
                        var notification = new Notification(
                                "An unexpected error has occurred. Please try again later.");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setPosition(Notification.Position.TOP_CENTER);
                        notification.setDuration(3000);
                        ui.access(notification::open);
                    });
                }));
    }
}
