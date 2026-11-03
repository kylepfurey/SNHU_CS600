// .java
// Sunrise/Sunset API Service
// by Kyle Furey

package com.snhu.mscs.cs600.sunfeature;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestClientException;
import java.util.Map;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.json.JsonParseException;

/**
 * API Service for Sunrise and Sunset
 */
@Service
public class SunService {
    /**
     * API Data
     */
    final RestTemplate restTemplate;
    final String url;
    final double latitude;
    final double longitude;
    static final Logger logger = LoggerFactory.getLogger(SunService.class);

    /**
     * Constructor
     */
    public SunService(
            RestTemplate restTemplate,
            @Value("${sunrise.api.url}") String url,
            @Value("${sunrise.location.latitude}") double latitude,
            @Value("${sunrise.location.longitude}") double longitude
    ) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * API
     */
    public SunData getSunData() {
        String uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("lat", latitude)
                .queryParam("lng", longitude)
                .toUriString();

        String body;
        try {
            body = restTemplate.getForObject(uri, String.class);
        } catch (RestClientException e) {
            logger.error("Sunrise/sunset API call failed: {}", e.getMessage());
            throw new SunUnavailableException("Could not reach the sunrise/sunset service.", e);
        }

        if (body == null || body.isBlank()) {
            logger.error("Sunrise/sunset API returned an empty response");
            throw new SunUnavailableException("The service returned no data.");
        }

        Map<String, Object> json;
        try {
            json = JsonParserFactory.getJsonParser().parseMap(body);
        } catch (JsonParseException e) {
            logger.error("Sunrise/sunset API returned invalid JSON", e);
            throw new SunUnavailableException("The service returned an unreadable response.", e);
        }

        if (!"OK".equals(json.get("status"))) {
            logger.error("Sunrise/sunset API reported status: {}", json.get("status"));
            throw new SunUnavailableException("The service reported a problem with the request.");
        }

        if (!(json.get("results") instanceof Map<?, ?> results) ||
                !(results.get("sunrise") instanceof String sunrise) ||
                !(results.get("sunset") instanceof String sunset) ||
                sunrise.isBlank() ||
                sunset.isBlank()) {
            logger.error("Sunrise/sunset API response is missing expected fields");
            throw new SunUnavailableException("The service response was missing sunrise or sunset data.");
        }

        return new SunData(sunrise, sunset);
    }
}
