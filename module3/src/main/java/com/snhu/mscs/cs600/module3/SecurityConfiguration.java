// .java
// Spring Security Configuration
// by Kyle Furey

package com.snhu.mscs.cs600.module3;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;

/** PasswordEncoder Bean */
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** UserDetailsService Bean */
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/** SecurityFilterChain Bean */
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Spring Security Configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    /**
     * Beans
     */

    /**
     * This method returns the encoding for passwords.
     * For this configuration, we use BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * This method defines how users are looked up.
     * We currently only enable one user access.
     * All other users are denied with 401.
     * Credentials are kept in memory using InMemoryUserDetailsManager.
     */
    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder encoder,
            @Value("${app.security.username}") String username,
            @Value("${app.security.password}") String password
    ) {
        UserDetails user = User.withUsername(username)
                .password(encoder.encode(password))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
    /**
     * This method defines what requests are valid, filtering invalid requests.
     * We do not allow any unauthenticated users.
     * We are stateless, and do not cache users between requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
