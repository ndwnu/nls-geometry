package nu.ndw.nls.geometry.jts.validation;

import org.locationtech.jts.geom.Coordinate;

/**
 * Utility class for validating geographic coordinates based on the WGS84 standard.
 * <p>
 * The World Geodetic System 1984 (WGS84) is the reference coordinate system used by the Global Positioning System (GPS). This class
 * provides functionality to check whether specified latitude and longitude values fall within the valid ranges defined by the WGS84
 * standard.
 * <p>
 * Key Features:
 * <ul>
 * <li>Validation of longitude values, ensuring they are within the range -180 to 180 degrees.</li>
 * <li>Validation of latitude values, ensuring they are within the range -90 to 90 degrees.</li>
 * <li>Validation of coordinate pairs to ensure both longitude and latitude are valid.</li>
 * </ul>
 * <p>
 * Thread Safety: This class consists of static methods and contains no mutable state, making it thread-safe.
 * <p>
 * Use Cases:
 * <ul>
 * <li>Verify if input coordinates (latitude and longitude) comply with WGS84 standards.</li>
 * <li>Precondition checks for systems that require valid GPS coordinates.</li>
 * </ul>
 */
public final class Wgs84CoordinateValidator {

    private static final int MAX_LONGITUDE = 180;

    private static final int MIN_LONGITUDE = -MAX_LONGITUDE;

    private static final int MAX_LATITUDE = 90;

    private static final int MIN_LATITUDE = -MAX_LATITUDE;

    private Wgs84CoordinateValidator() {
        // Utility class.
    }

    public static boolean isValidWgs84Coordinate(Coordinate coordinate) {
        return isValidLongitude(coordinate.x) && isValidLatitude(coordinate.y);
    }

    private static boolean isValidLongitude(double longitude) {
        return longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE;
    }

    private static boolean isValidLatitude(double latitude) {
        return latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE;
    }
}
