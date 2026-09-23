package com.snhu.mscs.cs600.salesguru;

/**
 * Imports to allow for the configuration of our Custom Security Adapter with
 * the Spring Security Framework.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * CustomSecurityAppConfig - 
 * 
 * Purpose:
 *      The purpose of this class is to configure the necessary parameters
 *      to enable Spring Security for our Web Application. This will allow
 *      for authentication and authorization of users to control access to
 *      our API.
 * 
 * Class Level Annotations:
 *      @Configuration - This tells the Spring Framework that this class
 *      is going to have configuration items that are pulled from other
 *      parts of the Framework.
 * 
 *      @EnableWebSecurity - This annotation enables web security features
 *      in Spring Security to allow for the implemenation of authentication
 *      and authorization of users. 
 * 
 * Sample Output: N/A
 *  
 * Constraints: N/A
 * 
 * Expected Results:
 *      The proper configuration of this class will require anyone
 *      trying to get data from an API endpoint to authenticate. However
 *      it will permit anyone to query the /version endpoint without hinderance.
 * 
 * Version              Author      Date            Description
 * ***********************************************************************
 *     1                 fjm        6-Oct-2025     Initial Creation
 */
@Configuration
@EnableWebSecurity
public class CustomSecurityAppConfig {

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
     * userDetailsService - This is a bean that creates a single test user and
     * stores the details in memory to demonstrate the functionality of Spring
     * Security in our application. In production, this bean would change to 
     * pull user credentials from a database.
     * 
     * @return UserDetailsService that can authenticate our static user.
     */
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("sgUser")
            .password(passwordEncoder().encode("salesGuru.123"))
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * securityFilterChain - Method that configures a security filter chain
     * to apply the necessary access controls to an HTTP data stream. This
     * method utilizes a Lambda function to chain multiple filters and 
     * constraints to allow processing more than one item in the chain.
     * 
     * This methodology requires thoughtful architecture to reduce the numnber
     * branches that must be supported.
     * 
     * This implementation of the SecurityFilterChain validates Requirement #4, "The 
     * application shall provide a mechanism to restrict access to only authenticated
     * users."
     * 
     * @param http object containing the URL information necessary to make decisions.
     * @return SecurityFilterChain that can be used for fast processing of accessible
     * data.
     * @throws Exception - Generic exception is used here because many security 
     * related exceptions are too broad.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/v1/version").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Enable Basic Auth
        return http.build();
    }
}