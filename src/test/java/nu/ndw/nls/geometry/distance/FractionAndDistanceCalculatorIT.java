package nu.ndw.nls.geometry.distance;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nu.ndw.nls.geometry.GeometryConfiguration;
import nu.ndw.nls.geometry.distance.model.CoordinateAndBearing;
import nu.ndw.nls.geometry.distance.model.FractionAndDistance;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GeometryConfiguration.class})
class FractionAndDistanceCalculatorIT {

    private static final Coordinate FROM = new Coordinate(5.42670371, 52.17673587);
    private static final Coordinate TO = new Coordinate(5.42672895, 52.17670980);
    private static final LineString LINE_STRING = new GeometryFactoryWgs84().createLineString(
            new Coordinate[]{FROM, TO});
    private static final LineString LINE_STRING_RD = new GeometryFactoryRijksdriehoek().createLineString(
            new Coordinate[]{FROM, TO});
    private static final double FRACTION_DISTANCE_DELTA = 0.00005;
    private static final double COORDINATE_DELTA = 0.0000001;
    private static final double BEARING_DELTA = 0.5;
    private static final String ERROR_MESSAGE = "SRID must be WGS84 and is RIJKSDRIEHOEK";

    @Autowired
    private FractionAndDistanceCalculator fractionAndDistanceCalculator;

    @Test
    void calculateFractionAndDistance_ok() {
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(
                LINE_STRING, new Coordinate(5.426716016, 52.17672277));
        assertEquals(0.4953, fractionAndDistance.getFraction(), FRACTION_DISTANCE_DELTA);
        assertEquals(1.6719, fractionAndDistance.getFractionDistance(), FRACTION_DISTANCE_DELTA);
        assertEquals(3.3758, fractionAndDistance.getTotalDistance(), FRACTION_DISTANCE_DELTA);
    }

    @Test
    void calculateFractionAndDistance_ok_no_srid() {
        LineString lineString = new GeometryFactoryWgs84().createLineString(
                new Coordinate[]{FROM, TO});
        lineString.setSRID(0);
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(
                lineString, new Coordinate(5.426716016, 52.17672277));
        assertEquals(0.4953, fractionAndDistance.getFraction(), FRACTION_DISTANCE_DELTA);
        assertEquals(1.6719, fractionAndDistance.getFractionDistance(), FRACTION_DISTANCE_DELTA);
        assertEquals(3.3758, fractionAndDistance.getTotalDistance(), FRACTION_DISTANCE_DELTA);
    }


    @Test
    void calculateFractionAndDistance_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fractionAndDistanceCalculator.calculateFractionAndDistance(
                        LINE_STRING_RD, new Coordinate(5.426716016, 52.17672277)))
                .withMessage(ERROR_MESSAGE);


    }

    @Test
    void calculateFractionAndDistance_ok_beforeStartPointMinimum0() {
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(
                LINE_STRING, new Coordinate(5.426702865, 52.176737201));
        assertEquals(0.0, fractionAndDistance.getFraction(), FRACTION_DISTANCE_DELTA);
        assertEquals(0.0, fractionAndDistance.getFractionDistance(), FRACTION_DISTANCE_DELTA);
        assertEquals(3.3758, fractionAndDistance.getTotalDistance(), FRACTION_DISTANCE_DELTA);
    }

    @Test
    void calculateFractionAndDistance_ok_afterEndPointMaximum1() {
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(
                LINE_STRING, new Coordinate(5.426729913, 52.17670903));
        assertEquals(1.0, fractionAndDistance.getFraction(), FRACTION_DISTANCE_DELTA);
        assertEquals(3.3758, fractionAndDistance.getFractionDistance(), FRACTION_DISTANCE_DELTA);
        assertEquals(3.3758, fractionAndDistance.getTotalDistance(), FRACTION_DISTANCE_DELTA);
    }

    @Test
    void getSubLineStringByLengthInMeters_ok() {

        LineString originalLineString = createLineString(
                new Coordinate(5.0, 53.0),
                new Coordinate(5.0, 53.01),
                new Coordinate(5.0, 53.02));

        LineString subLineString = fractionAndDistanceCalculator.getSubLineStringByLengthInMeters(originalLineString,
                1);

        assertEquals(
                createLineString(
                        new Coordinate(5.0, 53.0),
                        new Coordinate(5.0, 53.00000898583448)),
                subLineString);
    }

    @Test
    void getSubLineStringByLengthInMeters_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fractionAndDistanceCalculator.getSubLineStringByLengthInMeters(
                        LINE_STRING_RD, 2))
                .withMessage(ERROR_MESSAGE);


    }

    @Test
    void getSubLineStringByLengthInMeters_ok_distanceInMetersIsLargerThatOriginalLineString() {

        LineString originalLineString = createLineString(
                new Coordinate(5.0, 53.0),
                new Coordinate(5.0, 53.01),
                new Coordinate(5.0, 53.02));

        LineString subLineString = fractionAndDistanceCalculator.getSubLineStringByLengthInMeters(originalLineString,
                100000);

        assertEquals(originalLineString, subLineString);
    }

    @Test
    void calculateLengthInMeters_ok() {
        double lengthInMeters = fractionAndDistanceCalculator.calculateLengthInMeters(LINE_STRING);
        assertEquals(3.3758, lengthInMeters, FRACTION_DISTANCE_DELTA);
    }

    @Test
    void calculateLengthInMeters_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fractionAndDistanceCalculator.calculateLengthInMeters(LINE_STRING_RD))
                .withMessage(ERROR_MESSAGE);
    }

    @Test
    void getSubLineString_ok_betweenPoints() {
        LineString originalLineString = createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.01));
        LineString subLineString = fractionAndDistanceCalculator.getSubLineString(originalLineString, 0.5);
        // Because we use the geodetic calculator, the answer is expected to slightly deviate from 53.005, which would
        // be the result of simple Cartesian interpolation.
        assertEquals(2, subLineString.getNumPoints());
        assertEquals(5.0, subLineString.getCoordinateN(0).getX());
        assertEquals(53.0, subLineString.getCoordinateN(0).getY());
        assertEquals(5.0, subLineString.getCoordinateN(1).getX());
        assertEquals(53.005, subLineString.getCoordinateN(1).getY(), COORDINATE_DELTA);
    }

    @Test
    void getSubLineString_ok_onPoint() {
        LineString originalLineString = createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.01),
                new Coordinate(5.0, 53.02));
        LineString subLineString = fractionAndDistanceCalculator.getSubLineString(originalLineString, 0.5);
        assertEquals(createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.01)), subLineString);
    }

    @Test
    void getSubLineString_ok_zero() {
        LineString originalLineString = createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.01));
        LineString subLineString = fractionAndDistanceCalculator.getSubLineString(originalLineString, 0.0);
        assertEquals(createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.0)), subLineString);
    }

    @Test
    void getSubLineString_ok_one() {
        LineString originalLineString = createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.01));
        LineString subLineString = fractionAndDistanceCalculator.getSubLineString(originalLineString, 1.0);
        assertEquals(originalLineString, subLineString);
    }

    @Test
    void getSubLineString_ok_startEnd() {
        LineString originalLineString = createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.01));
        LineString subLineString = fractionAndDistanceCalculator.getSubLineString(originalLineString, 0.25, 0.75);
        // Because we use the geodetic calculator, the answer is expected to slightly deviate from 53.0025 and 53.0075,
        // which would be the result of simple Cartesian interpolation.
        assertEquals(2, subLineString.getNumPoints());
        assertEquals(5.0, subLineString.getCoordinateN(0).getX());
        assertEquals(53.0025, subLineString.getCoordinateN(0).getY(), COORDINATE_DELTA);
        assertEquals(5.0, subLineString.getCoordinateN(1).getX());
        assertEquals(53.0075, subLineString.getCoordinateN(1).getY(), COORDINATE_DELTA);
    }

    @Test
    void getSubLineString_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fractionAndDistanceCalculator.getSubLineString(LINE_STRING_RD, 0.5))
                .withMessage(ERROR_MESSAGE);
    }

    @Test
    void getSubLineStringStartEnd_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fractionAndDistanceCalculator.getSubLineString(LINE_STRING_RD, 0.5, 1.0))
                .withMessage(ERROR_MESSAGE);
    }


    @Test
    void getCoordinateAndBearing_ok() {
        LineString originalLineString = createLineString(FROM, TO);
        CoordinateAndBearing coordinateAndBearing = fractionAndDistanceCalculator.getCoordinateAndBearing(
                originalLineString, 0.5);
        assertEquals(149, coordinateAndBearing.bearing(), BEARING_DELTA);
    }


    @Test
    void getCoordinateAndBearing_exception() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fractionAndDistanceCalculator.getCoordinateAndBearing(LINE_STRING_RD, 0.5))
                .withMessage(ERROR_MESSAGE);
    }

    private LineString createLineString(Coordinate... coordinates) {
        return new GeometryFactoryWgs84().createLineString(coordinates);
    }

}
