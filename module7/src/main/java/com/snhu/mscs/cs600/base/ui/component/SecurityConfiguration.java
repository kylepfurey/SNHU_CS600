package com.snhu.mscs.cs600.base.ui.component;

/**
 * Imports to allow for the configuration of our Custom Security Adapter with
 * the Spring Security Framework.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Vaadin specifc imports to integrate with Spring Security
 */
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;

/**
 * The application specific Login View.
 */
import com.snhu.mscs.cs600.base.ui.LoginView;

/**
 * SecurityConfiguration - 
 * 
 * Purpose:
 *      The purpose of this class is to provide the configuration of
 *      application security. This class is always customized to the 
 *      application and there is no default naming convention for the class.
 *      The @EnableWebSecurity annotation flags the class to Spring Security
 *      as providing the necessary elements to configure the security
 *      environment.
 * 
 * Extends: 
 *      N/A
 *
 * Class Level Annotations:
 *      @EnableWebSecurity - Annotation to indicate to Spring Security
 *      that this class provides the security configuration.
 *      @Configuration - Annotation indicating that this class is a source
 *      of Bean definitions.
 *      @Import - Indicates one or more component classes to import - usually
 *      configuration classes.
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
@EnableWebSecurity 
@Configuration
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class) 
public class SecurityConfiguration {


    /**
     * passwordEncoder - This is a bean the provides a utility method for
     * encoding a user submitted password to match for authentication.
     * 
     * @return A BCryptPasswordEncoder object that can be used to create a
     *         cryptographic hash of a password for secure storage.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * securityFilterChain - This method creates a SecurityFilterChain that is used to
     * intercept calls to various portions of the application and validate that the 
     * entity making the call is authorized to do so.
     * 
     * @param http An HttpSecurity object that provides access to the particulars of an
     *      http conversation between source and Endpoint.
     * @return SecurityFilterChain object that will be used to implement security
     *      restrictions. 
     * @throws Exception in the event of an issue with communications.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure any static resources with public access. This could include
        // logos or other UI or informational material that does not require 
        // authentication.
        http.authorizeHttpRequests(auth -> 
                auth.requestMatchers("/static/**").permitAll());

        // Configure Vaadin's security using VaadinSecurityConfigurer
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> { 
            // This is important to register your login view to the
            // navigation access control mechanism:
            configurer.loginView(LoginView.class); 

            // You can add any possible extra configurations of your own
            // here (the following is just an example):
            // configurer.enableCsrfConfiguration(false);
        });

        // Return the configured HttpSecurity object
        return http.build();
    }

    /**
     * UserDetailsManager only provides two hardcoded in-memory users 
     * and their roles.
     * 
     * NOTE: This is for DEMONSTRATION PURPOSES ONLY! This should NEVER 
     * be used in real world applications.
     * 
     * CHALLENGE: Re-write this method to pull data from a database
     * table and make this production ready code.
     */
    @Bean
    UserDetailsManager userDetailsService() {
        UserDetails user =
                User.withUsername("user")
                        .password(passwordEncoder().encode("user"))
                        .roles("USER")
                        .build();
        UserDetails admin =
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("USER", "ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}