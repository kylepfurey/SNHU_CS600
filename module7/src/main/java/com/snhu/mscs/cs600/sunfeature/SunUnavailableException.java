// .java
// Sunrise/Sunset Unavailability Exception
// by Kyle Furey

package com.snhu.mscs.cs600.sunfeature;

/**
 * Thrown when sunrise/sunset data cannot be retrieved or understood.
 */
public class SunUnavailableException extends RuntimeException {
    public SunUnavailableException(String message) { super(message); }
    public SunUnavailableException(String message, Throwable cause) { super(message, cause); }
}
