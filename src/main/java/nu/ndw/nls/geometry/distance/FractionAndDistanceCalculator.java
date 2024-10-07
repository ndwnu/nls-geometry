package nu.ndw.nls.geometry.distance;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import nu.ndw.nls.geometry.bearing.BearingCalculator;
import nu.ndw.nls.geometry.constants.SRID;
import nu.ndw.nls.geometry.distance.model.CoordinateAndBearing;
import nu.ndw.nls.geometry.distance.model.FractionAndDistance;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import nu.ndw.nls.geometry.factories.GeometryFactorySrid;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FractionAndDistanceCalculator {

    private static final double DISTANCE_TOLERANCE_1_CM = 0.01;
    private final GeodeticCalculatorFactory geodeticCalculatorFactory;
    private final Map<SRID, GeometryFactorySrid> geometryFactorySridMap;
    private final BearingCalculator bearingCalculator;

    public FractionAndDistanceCalculator(GeodeticCalculatorFactory geodeticCalculatorFactory,
            List<GeometryFactorySrid> geometryFactories, BearingCalculator bearingCalculator) {
        this.geodeticCalculatorFactory = geodeticCalculatorFactory;
        this.geometryFactorySridMap = geometryFactories.stream()
                .collect(Collectors.toMap(GeometryFactorySrid::getSrid, Function.identity()));
        this.bearingCalculator = bearingCalculator;
    }

    public FractionAndDistance calculateFractionAndDistance(LineString line, Coordinate inputCoordinate) {
        SRID srid = SRID.fromValue(line.getSRID());
        GeodeticCalculator geodeticCalculator = geodeticCalculatorFactory.createGeodeticCalculator(srid);
        LocationIndexedLine locationIndexedLine = new LocationIndexedLine(line);
        LinearLocation snappedPointLocation = locationIndexedLine.project(inputCoordinate);
        Coordinate snappedPointCoordinate = snappedPointLocation.getCoordinate(line);
        Coordinate[] coordinates = line.getCoordinates();
        double sumOfPathLengths = 0;
        Double pathDistanceToSnappedPoint = null;
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate current = coordinates[i];
            if (i == snappedPointLocation.getSegmentIndex()) {
                pathDistanceToSnappedPoint = sumOfPathLengths + calculateDistance(current, snappedPointCoordinate,
                        geodeticCalculator);
            }
            if (i + 1 < coordinates.length) {
                Coordinate next = coordinates[i + 1];
                sumOfPathLengths += calculateDistance(current, next, geodeticCalculator);
            }
        }
        if (pathDistanceToSnappedPoint == null) {
            throw new IllegalStateException("Failed to find path distance to snapped point");
        }
        double fraction = sumOfPathLengths > 0 ? (pathDistanceToSnappedPoint / sumOfPathLengths) : sumOfPathLengths;

        log.trace("Total (geometrical) edge length: {}, snapped point path length {}. Fraction: {}",
                sumOfPathLengths,
                pathDistanceToSnappedPoint, fraction);
        return FractionAndDistance
                .builder()
                .fraction(fraction)
                .fractionDistance(pathDistanceToSnappedPoint)
                .totalDistance(sumOfPathLengths)
                .build();
    }

    public double calculateLengthInMeters(LineString lineString) {
        GeodeticCalculator geodeticCalculator = geodeticCalculatorFactory.createGeodeticCalculator(SRID.fromValue(
                lineString.getSRID()));
        Coordinate[] coordinates = lineString.getCoordinates();
        return IntStream.range(1, coordinates.length)
                .mapToDouble(
                        index -> calculateDistance(coordinates[index - 1], coordinates[index], geodeticCalculator))
                .sum();
    }

    public LineString getSubLineStringByLengthInMeters(LineString lineString, double distanceInMeters) {

        double lineStringLengthInMeters = calculateLengthInMeters(lineString);
        if (distanceInMeters >= lineStringLengthInMeters) {
            return lineString;
        }

        double fraction = distanceInMeters / lineStringLengthInMeters;

        return getSubLineString(lineString, fraction);
    }

    /**
     * Extract a subsection from the provided lineString, starting at 0 and ending at the provided fraction.
     */
    public LineString getSubLineString(LineString lineString, double fraction) {
        return getSubLineStringAndLastBearing(lineString, fraction).subLineString();
    }

    /**
     * Extract a subsection from the provided lineString, starting and ending at the provided fractions. This is a quick
     * and dirty implementation that calls getSubLineStringAndLastBearingByMetres twice, because it currently only
     * supports end metres.
     * TODO Add support for start metres to getSubLineStringAndLastBearingByMetres, so we only have to call it once.
     */
    public LineString getSubLineString(LineString lineString, double startFraction, double endFraction) {
        double length = calculateLengthInMeters(lineString);
        double startMetres = startFraction * length;
        double endMetres = endFraction * length;
        LineString zeroToEnd = getSubLineStringByMetres(lineString, endMetres);
        return getSubLineStringByMetres(zeroToEnd.reverse(), endMetres - startMetres).reverse();
    }

    private LineString getSubLineStringByMetres(LineString lineString, double fractionLength) {
        return getSubLineStringAndLastBearingByMetres(lineString, fractionLength).subLineString();
    }

    public CoordinateAndBearing getCoordinateAndBearing(LineString lineString, double fraction) {
        SubLineStringAndLastBearing subLineStringAndLastBearing = getSubLineStringAndLastBearing(lineString, fraction);
        return CoordinateAndBearing
                .builder()
                .bearing(subLineStringAndLastBearing.lastBearing())
                .coordinate(subLineStringAndLastBearing.getLastCoordinate())
                .build();
    }

    private SubLineStringAndLastBearing getSubLineStringAndLastBearing(LineString lineString, double fraction) {
        double fractionLength = fraction * calculateLengthInMeters(lineString);
        return getSubLineStringAndLastBearingByMetres(lineString, fractionLength);
    }

    private SubLineStringAndLastBearing getSubLineStringAndLastBearingByMetres(LineString lineString,
            double fractionLength) {
        SRID srid = SRID.fromValue(lineString.getSRID());
        GeodeticCalculator geodeticCalculator = geodeticCalculatorFactory.createGeodeticCalculator(srid);
        double sumOfPathLengths = 0;
        Coordinate[] coordinates = lineString.getCoordinates();
        List<Coordinate> result = new ArrayList<>();
        double lastBearing = 0;
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate current = coordinates[i];
            result.add(current);
            if (i + 1 < coordinates.length) {
                Coordinate next = coordinates[i + 1];
                geodeticCalculator.setStartingGeographicPoint(current.getX(), current.getY());
                geodeticCalculator.setDestinationGeographicPoint(next.getX(), next.getY());
                lastBearing = geodeticCalculator.getAzimuth();
                double lengthBefore = sumOfPathLengths;
                sumOfPathLengths += geodeticCalculator.getOrthodromicDistance();
                if (fractionLength >= lengthBefore && fractionLength < sumOfPathLengths) {
                    double distance = fractionLength - lengthBefore;
                    // Don't introduce an extra point if it's within 1 cm of the last point from the original geometry.
                    if (distance > DISTANCE_TOLERANCE_1_CM) {
                        geodeticCalculator.setDirection(lastBearing, distance);
                        Point2D point = geodeticCalculator.getDestinationGeographicPoint();
                        result.add(new Coordinate(point.getX(), point.getY()));
                    } else if (i == 0) {
                        // Make sure the result always consists of at least two coordinates.
                        result.add(current);
                    }
                    break;
                }
            }
        }
        GeometryFactory geometryFactory = getGeometryFactory(srid);
        return SubLineStringAndLastBearing
                .builder()
                .subLineString(geometryFactory.createLineString(result.toArray(new Coordinate[0])))
                .lastBearing(bearingCalculator.normaliseBearing(lastBearing))
                .build();
    }

    private GeometryFactory getGeometryFactory(SRID srid) {
        if (geometryFactorySridMap.containsKey(srid)) {
            return (GeometryFactory) geometryFactorySridMap.get(srid);
        } else {
            throw new IllegalArgumentException("SRID " + srid + " not supported");
        }
    }

    private static double calculateDistance(Coordinate from, Coordinate to, GeodeticCalculator geodeticCalculator) {
        geodeticCalculator.setStartingGeographicPoint(to.getX(), to.getY());
        geodeticCalculator.setDestinationGeographicPoint(from.getX(), from.getY());
        return geodeticCalculator.getOrthodromicDistance();
    }

    @Builder
    private record SubLineStringAndLastBearing(LineString subLineString, double lastBearing) {

        Coordinate getLastCoordinate() {
            int lastIndex = subLineString.getNumPoints() - 1;
            return subLineString.getCoordinateN(lastIndex);
        }
    }
}
