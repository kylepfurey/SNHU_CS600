package com.snhu.mscs.cs600;

/**
 * Imports to support a basic Vaadin UI running with Spring Boot.
 */
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the startup of the 
 *      SpringBoot Environment and initiate the Vaadin UI application.
 * 
 * Implements: 
 *      AppShellConfiguratpr - This interface is specific to Vaadin and
 *      is used to configure application features and the initial host
 *      page where Vaadin is running.
 *
 * Class Level Annotations:
 *      @SpringBootApplication - Indicates that this is running within
 *      SpringBoot. Vaadin applications can be built to run as stand-alone
 *      apps, or within other environments.
 *      @Theme - This indicates the Theme that is used to style the Vaadin
 *      application. This allows for incredible customization capabilities.
 *      Here, we are utilizing the default theme.
 * 
 * Sample Output: N/A
 * 
 * Constraints: N/A
 * 
 * Expected Results: 
 *      This class will be used to start the application.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        26-Oct-2025     Initial Creation
 */
@SpringBootApplication
@Theme("default")
public class Application implements AppShellConfigurator {

    /**
     * Main - Method to start the application.
     * 
     * @param args String[] for command line parameters, not used in this
     *             application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
