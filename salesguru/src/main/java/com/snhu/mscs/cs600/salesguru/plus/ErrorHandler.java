// .java
// Error Handler
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.plus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.springframework.http.HttpStatus;
import java.util.NoSuchElementException;

/**
 * Handles Thrown API Errors
 */
@RestControllerAdvice
public class ErrorHandler {
    /**
     * Logger
     */
    static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", error.getMessage()));
    }

    /**
     * NoSuchElementException
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(NoSuchElementException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", error.getMessage()));
    }

    /**
     * IllegalStateException
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> serverError(IllegalStateException error) {
        logger.error("Server error: ", error.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Server error"));
    }
}
