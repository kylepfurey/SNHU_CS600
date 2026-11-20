// .java
// Initial User Creator
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.plus;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.core.userdetails.User;

/**
 * Creates Initial User
 */
@Component
public class UserBootstrap implements ApplicationRunner {
    /**
     * Fields
     */
    final JdbcUserDetailsManager users;
    final PasswordEncoder encoder;
    final String username;
    final String password;

    /**
     * Constructor
     */
    public UserBootstrap(
            JdbcUserDetailsManager users,
            PasswordEncoder encoder,
            @Value("${salesguru.seed.username:}")
            String username,
            @Value("${salesguru.seed.password:}")
            String password
    ) {
        this.users = users;
        this.encoder = encoder;
        this.username = username;
        this.password = password;
    }

    /**
     * Bootstrap Method
     */
    @Override
    public void run(ApplicationArguments args) {
        if (username.isBlank() || password.isBlank() || users.userExists(username)) {
            return;
        }
        users.createUser(
                User.withUsername(username)
                        .password(encoder.encode(password))
                        .roles("USER")
                        .build()
        );
    }
}
