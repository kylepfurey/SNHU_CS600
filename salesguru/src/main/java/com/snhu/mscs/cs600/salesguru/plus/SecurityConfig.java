package com.snhu.mscs.cs600.salesguru.plus;

/**
 * Imports to allow for the configuration of our Custom Security Adapter with
 * the Spring Security Framework.
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Import;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.sql.DataSource;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import com.snhu.mscs.cs600.salesguru.plus.ui.LoginView;

/**
 * SecurityConfig -
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
 *      @Import - This annotation allows importing a configuration class.
 *      We use VaadinAwareSecurityContextHolderStrategyConfiguration to
 *      allow Vaadin's built-in security in Spring Security.
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
 *     2                 kpf       19-Nov-2026     Upgraded Security (auth database)
 */
@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

    /**
     * passwordEncoder - This is a bean the provides a utility method for
     * encoding a user submitted password to match for authentication.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * userDetailsService - This is a bean that creates a single test user and
     * stores the details in memory to demonstrate the functionality of Spring
     * Security in our application. This pulls from our authentication database.
     */
    @Bean
    JdbcUserDetailsManager userDetailsService(
            @Qualifier("authDataSource")
            DataSource authDataSource
    ) {
        return new JdbcUserDetailsManager(authDataSource);
    }

    /**
     * apiFilterChain - Method that configures an API filter chain
     * to apply the necessary access controls to an HTTP data stream. This
     * method utilizes a Lambda function to chain multiple filters and
     * constraints to allow processing more than one item in the chain.
     */
    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/version")
                            .permitAll()
                            .anyRequest()
                            .authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    /**
     * vaadinFilterChain - Method that configures a security filter chain
     * for Vaadin's generated code.
     */
    @Bean
    @Order(2)
    SecurityFilterChain vaadinFilterChain(HttpSecurity http) throws Exception {
        http.with(
                VaadinSecurityConfigurer.vaadin(),
                configurer -> configurer.loginView(LoginView.class)
        );
        return http.build();
    }
}
