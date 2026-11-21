// .java
// Security Testing Suite
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Security Testing
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTests {
    /**
     * Services
     */
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ServerProperties serverProperties;
    @Value("${salesguru.seed.username:}")
    String seedUsername;
    @Value("${salesguru.seed.password:}")
    String seedPassword;

    /**
     * Security Tests
     */
    @Test
    void getRequiresAuth() throws Exception {
        for (String path : new String[]{"/api/v1/companies", "/api/v1/representatives",
                "/api/v1/repsForCompany/1000", "/api/v1/items", "/api/v1/invoices"}) {
            mockMvc.perform(get(path))
                    .andExpect(status()
                            .isUnauthorized());
        }
    }
    @Test
    void postRequiresAuth() throws Exception {
        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"x\",\"cost\":1,\"retail\":2}"))
                .andExpect(status()
                        .isUnauthorized());
    }
    @Test
    void rejectBadCreds() throws Exception {
        mockMvc.perform(get("/api/v1/companies")
                        .header("Authorization", buildCreds("nobody", "wrong-password")))
                .andExpect(status()
                        .isUnauthorized());
    }
    @Test
    void acceptGoodCreds() throws Exception {
        assumeFalse(seedUsername.isBlank() || seedPassword.isBlank(), "No seed user configured");
        mockMvc.perform(get("/api/v1/companies")
                        .header("Authorization", buildCreds(seedUsername, seedPassword)))
                .andExpect(status()
                        .isOk());
    }
    @Test
    void httpsEnabledWithKeystore() {
        assertNotNull(serverProperties.getSsl());
        assertTrue(serverProperties.getSsl()
                .isEnabled());
        assertNotNull(serverProperties.getSsl()
                .getKeyStore());
    }
    @Test
    void cookieIsSecure() {
        assertEquals(
                Boolean.TRUE,
                serverProperties.getServlet()
                        .getSession()
                        .getCookie()
                        .getSecure()
        );
        assertEquals(
                Boolean.TRUE,
                serverProperties.getServlet()
                        .getSession()
                        .getCookie()
                        .getHttpOnly()
        );
    }

    /**
     * Helpers
     */
    String buildCreds(String username, String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password)
                        .getBytes(StandardCharsets.UTF_8));
    }
}
