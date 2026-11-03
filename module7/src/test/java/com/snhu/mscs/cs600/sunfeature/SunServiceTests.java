// .java
// Sunrise/Sunset API Service Tests
// by Kyle Furey

package com.snhu.mscs.cs600.sunfeature;
import org.springframework.test.web.client.MockRestServiceServer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.client.RestTemplate;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import org.springframework.http.HttpMethod;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Assertions;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import org.junit.jupiter.api.Test;

/**
 * Testing for Sunrise and Sunset Service
 */
class SunServiceTests {
    static final String URL = "https://api.sunrisesunset.io/json";
    MockRestServiceServer server;
    SunService service;

    @BeforeEach
    void reset() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        service = new SunService(restTemplate, URL, 43.077225, -79.075393);
    }

    void respond(String body) {
        server.expect(requestTo(startsWith(URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));
    }

    @Test
    void testSunData() {
        respond("{\"results\":{\"sunrise\":\"6:50:52 AM\",\"sunset\":\"5:08:20 PM\"},\"status\":\"OK\"}");
        SunData data = service.getSunData();
        Assertions.assertEquals("6:50:52 AM", data.sunrise());
        Assertions.assertEquals("5:08:20 PM", data.sunset());
        server.verify();
    }

    @Test
    void catchServerError() {
        server.expect(requestTo(startsWith(URL))).andRespond(withServerError());
        Assertions.assertThrows(SunUnavailableException.class, () -> service.getSunData());
    }

    @Test
    void catchEmptyResponse() {
        respond("");
        Assertions.assertThrows(SunUnavailableException.class, () -> service.getSunData());
    }

    @Test
    void catchBadJson() {
        respond("EVIL TEXT");
        Assertions.assertThrows(SunUnavailableException.class, () -> service.getSunData());
    }

    @Test
    void catchMissingStatus() {
        respond("{\"results\":{\"sunrise\":\"6:50:52 AM\",\"sunset\":\"5:08:20 PM\"}}");
        Assertions.assertThrows(SunUnavailableException.class, () -> service.getSunData());
    }

    @Test
    void catchBadStatus() {
        respond("{\"results\":{\"sunrise\":\"6:50:52 AM\","
                + "\"sunset\":\"5:08:20 PM\"},\"status\":\"INVALID_REQUEST\"}");
        Assertions.assertThrows(SunUnavailableException.class, () -> service.getSunData());
    }

    @Test
    void catchMissingFields() {
        respond("{\"results\":{\"sunrise\":\"6:50:52 AM\"},\"status\":\"OK\"}");
        Assertions.assertThrows(SunUnavailableException.class, () -> service.getSunData());
    }
}
