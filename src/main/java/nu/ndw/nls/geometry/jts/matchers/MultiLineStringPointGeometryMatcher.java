package nu.ndw.nls.geometry.jts.matchers;

import static org.locationtech.jts.geom.util.LineStringExtracter.getLines;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.distance.FractionAndDistanceCalculator;
import nu.ndw.nls.geometry.distance.model.CoordinateAndBearing;
import nu.ndw.nls.geometry.distance.model.FractionAndDistance;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultiLineStringPointGeometryMatcher {

    private final FractionAndDistanceCalculator fractionAndDistanceCalculator;

    private final GeodeticCalculatorFactory geodeticCalculatorFactory;

    /**
     * Matches a given point to the closest line in a MultiLineString within a specified buffer distance. If multiple candidates are found,
     * the closest line is returned. Throws an exception if no matching line is found.
     *
     * @param multiLineString the MultiLineString geometry that contains the lines to check against
     * @param point           the Point geometry to be matched to the closest line
     * @param bufferInMetres  the maximum distance in meters within which a match is acceptable
     * @return the LineString from the MultiLineString that is closest to the point within the buffer distance
     * @throws IllegalStateException if no matching line is found within the specified buffer distance
     */
    public LineString matchPointOnMultiLine(MultiLineString multiLineString, Point point, int bufferInMetres) {
        //noinspection unchecked
        List<LineString> lines = getLines(multiLineString);
        return lines.stream()
                .map(line -> calculateDistanceToLine(point, line))
                .filter(p -> p.getLeft() <= bufferInMetres)
                .min(Comparator.comparingDouble(Pair::getLeft))
                .map(Pair::getRight)
                .orElseThrow(() -> new IllegalStateException("No matching line found for point %s".formatted(point)));
    }

    private Pair<Double, LineString> calculateDistanceToLine(Point point, LineString line) {
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(line,
                point.getCoordinate());
        CoordinateAndBearing coordinateAndBearing = fractionAndDistanceCalculator.getCoordinateAndBearing(line,
                fractionAndDistance.getFraction());
        GeodeticCalculator geodeticCalculator = geodeticCalculatorFactory.createGeodeticCalculator();
        geodeticCalculator.setStartingGeographicPoint(point.getX(), point.getY());
        geodeticCalculator.setDestinationGeographicPoint(coordinateAndBearing.coordinate().getX(),
                coordinateAndBearing.coordinate().getY());
        double distance = geodeticCalculator.getOrthodromicDistance();
        return new ImmutablePair<>(distance, line);
    }
}
