// .java
// Security Configuration
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.config;
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
import com.snhu.mscs.cs600.salesguru.ui.LoginView;

/**
 * Configures Security
 */
@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {
    /**
     * Password Encoding
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * User Details
     */
    @Bean
    JdbcUserDetailsManager userDetailsService(
            @Qualifier("authDataSource")
            DataSource authDataSource
    ) {
        return new JdbcUserDetailsManager(authDataSource);
    }

    /**
     * API Filtering
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
     * Vaadin Filtering
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
