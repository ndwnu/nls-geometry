package nu.ndw.nls.geometry.distance;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import nu.ndw.nls.geometry.factories.LocationIndexedLineFactory;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistanceCalculator {

    private final LocationIndexedLineFactory locationIndexedLineFactory;
    private final GeodeticCalculatorFactory geodeticCalculatorFactory;

    public double calculateDistance(Coordinate sourceCoordinate, LineString targetLineString) {
        LocationIndexedLine locationIndexedLine = locationIndexedLineFactory.createLocationIndexedLine(targetLineString);
        LinearLocation snappedLinearLocation = locationIndexedLine.project(sourceCoordinate);
        Coordinate snappedPointCoordinate = snappedLinearLocation.getCoordinate(targetLineString);

        GeodeticCalculator geodeticCalculator = geodeticCalculatorFactory.createGeodeticCalculator();
        geodeticCalculator.setStartingGeographicPoint(sourceCoordinate.getX(), sourceCoordinate.getY());
        geodeticCalculator.setDestinationGeographicPoint(snappedPointCoordinate.getX(), snappedPointCoordinate.getY());

        return geodeticCalculator.getOrthodromicDistance();
    }
}
