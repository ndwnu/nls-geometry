package nu.ndw.nls.geometry.bearing;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.bearing.model.BearingFilter;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BearingCalculator {

    private static final int MAX_BEARING = 360;

    private final GeodeticCalculatorFactory geodeticCalculatorFactory;

    public boolean bearingIsInRange(double actualBearing, BearingFilter bearingFilter) {
        // If no bearing filter is provided, return true so the match is always kept.
        if (bearingFilter == null) {
            return true;
        }
        return bearingDelta(actualBearing, bearingFilter.target()) <= bearingFilter.cutoffMargin();
    }

    public double bearingDelta(double actualBearing, double targetBearing) {
        double delta = Math.abs(actualBearing - targetBearing);
        return Math.min(delta, MAX_BEARING - delta);
    }

    public double calculateBearing(Coordinate currentCoordinate, Coordinate nextCoordinate) {
        GeodeticCalculator geodeticCalculator = geodeticCalculatorFactory.createGeodeticCalculator();
        geodeticCalculator.setStartingGeographicPoint(currentCoordinate.getX(), currentCoordinate.getY());
        geodeticCalculator.setDestinationGeographicPoint(nextCoordinate.getX(), nextCoordinate.getY());
        double bearing = geodeticCalculator.getAzimuth();
        return (bearing + MAX_BEARING) % MAX_BEARING;
    }

    public double normaliseBearing(double bearing) {
        return (bearing + MAX_BEARING) % MAX_BEARING;
    }
}
