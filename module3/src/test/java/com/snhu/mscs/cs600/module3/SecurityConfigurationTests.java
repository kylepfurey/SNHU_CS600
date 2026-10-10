// .java
// Spring Security Testing
// by Kyle Furey

package com.snhu.mscs.cs600.module3;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

/** Testing Objects */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetailsService;

/** API */
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.nio.charset.StandardCharsets;

/** Tests */
import org.junit.jupiter.api.Assertions;
import java.util.HashSet;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.containsString;

/**
 * Spring Security Tests
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTests {
    /**
     * Testing Objects
     */
    static final String BOOKS_ENDPOINT = "/api/v1/books";
    static final String VERSION_ENDPOINT = "/api/v1/version";
    @Autowired MockMvc mvc;
    @Value("${app.security.username}") String username;
    @Value("${app.security.password}") String password;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired UserDetailsService userDetailsService;

    /**
     * API
     */
    String[] getBooks() throws Exception {
        String body = mvc.perform(get(BOOKS_ENDPOINT).with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(body, String[].class);
        } catch (MismatchedInputException error) {
            // Note for professor:
            return Assertions.fail("The API returned an invalid JSON array. " +
                    "Please remove the '{' and '}' from APIController::getBooks parsing.", error);
        }
    }
    String getVersion() throws Exception {
        return mvc.perform(get(VERSION_ENDPOINT).with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    /**
     * Sanity Test
     */
    @Test
    void contextLoads() {}

    /**
     * Original Test Plan
     */
    @Test
    void booksEndpoint() throws Exception {
        String[] books = getBooks();
        Assertions.assertEquals(TestData.book_list.length, books.length);
        for (int i = 0; i < books.length; ++i) {
            Assertions.assertEquals(TestData.book_list[i], books[i]);
        }
    }
    @Test
    void versionEndpoint() throws Exception {
        Assertions.assertEquals("Version-1.0", getVersion());
    }
    @Test
    @Disabled("Unable to test - no ids present in dataset")
    void uniqueIds() {
    }
    @Test
    void noNulls() throws Exception {
        for (String entry : getBooks()) {
            Assertions.assertNotNull(entry);
            Assertions.assertFalse(entry.isBlank());
        }
    }
    @Test
    void noDuplicates() throws Exception {
        HashSet<String> titles = new HashSet<>();
        for (String entry : getBooks()) {
            Assertions.assertTrue(titles.add(entry));
        }
    }

    /**
     * New Test Plan
     */

    /**
     * Security Tests
     */
    @Test
    void denyNoCredentials() throws Exception {
        mvc.perform(get(VERSION_ENDPOINT))
                .andExpect(status().isUnauthorized());
        mvc.perform(get(BOOKS_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void denyBadUsername() throws Exception {
        mvc.perform(get(BOOKS_ENDPOINT).with(httpBasic("BAD", password)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void denyBadPassword() throws Exception {
        mvc.perform(get(VERSION_ENDPOINT).with(httpBasic(username, "BAD")))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void passwordIsEncoded() {
        String stored = userDetailsService.loadUserByUsername(username).getPassword();
        Assertions.assertNotEquals(password, stored);
        Assertions.assertTrue(stored.startsWith("$2"), "Expected a BCrypt hash");
    }

    /**
     * Vulnerability Tests
     */
    @Test
    void emptyResponseWhenUnauthenticated() throws Exception {
        mvc.perform(get(VERSION_ENDPOINT))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }
    @Test
    void apiIsStateless() throws Exception {
        mvc.perform(get(VERSION_ENDPOINT).with(httpBasic(username, password)))
                .andExpect(cookie().doesNotExist("JSESSIONID"));
    }
    @Test
    void denyPost() throws Exception {
        mvc.perform(post(BOOKS_ENDPOINT))
                .andExpect(status().is4xxClientError());
        mvc.perform(post(VERSION_ENDPOINT).with(httpBasic(username, password)))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Compliance Tests
     */
    @Test
    void headersArePresent() throws Exception {
        mvc.perform(get(VERSION_ENDPOINT).secure(true).with(httpBasic(username, password)))
                .andExpect(header().exists("X-Content-Type-Options"))
                .andExpect(header().exists("X-Frame-Options"))
                .andExpect(header().exists("Strict-Transport-Security"))
                .andExpect(header().string("Cache-Control", containsString("no-store")));
    }
    @Test
    void credentialsAreNotPresent() throws Exception {
        String body = mvc.perform(get(BOOKS_ENDPOINT).with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertFalse(body.contains(password));
        Assertions.assertFalse(body.contains("$2a$") || body.contains("$2b$"));
    }
}
