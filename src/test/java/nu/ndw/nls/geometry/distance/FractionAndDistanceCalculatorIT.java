package nu.ndw.nls.geometry.distance;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nu.ndw.nls.geometry.TestConfig;
import nu.ndw.nls.geometry.distance.model.CoordinateAndBearing;
import nu.ndw.nls.geometry.distance.model.FractionAndDistance;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class FractionAndDistanceCalculatorIT {

    private static final Coordinate FROM = new Coordinate(5.42670371, 52.17673587);
    private static final Coordinate TO = new Coordinate(5.42672895, 52.17670980);
    private static final LineString LINE_STRING = new GeometryFactoryWgs84().createLineString(
            new Coordinate[]{FROM, TO});
    private static final double DELTA = 0.00005;
    @Autowired
    private FractionAndDistanceCalculator fractionAndDistanceCalculator;


    @Test
    void calculateFractionAndDistance_ok() {
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(
                LINE_STRING, new Coordinate(5.426716016, 52.17672277));
        assertEquals(0.4953, fractionAndDistance.getFraction(), DELTA);
        assertEquals(1.6719, fractionAndDistance.getFractionDistance(), DELTA);
        assertEquals(3.3758, fractionAndDistance.getTotalDistance(), DELTA);
    }

    @Test
    void calculateFractionAndDistance_ok_beforeStartPointMinimum0() {
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(
                LINE_STRING, new Coordinate(5.426702865, 52.176737201));
        assertEquals(0.0, fractionAndDistance.getFraction(), DELTA);
        assertEquals(0.0, fractionAndDistance.getFractionDistance(), DELTA);
        assertEquals(3.3758, fractionAndDistance.getTotalDistance(), DELTA);
    }

    @Test
    void calculateFractionAndDistance_ok_afterEndPointMaximum1() {
        FractionAndDistance fractionAndDistance = fractionAndDistanceCalculator.calculateFractionAndDistance(
                LINE_STRING, new Coordinate(5.426729913, 52.17670903));
        assertEquals(1.0, fractionAndDistance.getFraction(), DELTA);
        assertEquals(3.3758, fractionAndDistance.getFractionDistance(), DELTA);
        assertEquals(3.3758, fractionAndDistance.getTotalDistance(), DELTA);
    }

    @Test
    void calculateLengthInMeters_ok() {
        double lengthInMeters = fractionAndDistanceCalculator.calculateLengthInMeters(LINE_STRING);
        assertEquals(3.3758, lengthInMeters, DELTA);
    }

    @Test
    void getSubLineString_ok_betweenPoints() {
        LineString originalLineString = createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.01));
        LineString subLineString = fractionAndDistanceCalculator.getSubLineString(originalLineString, 0.5);
        // Because we use the geodetic calculator, the answer is expected to slightly deviate from 53.005, which would
        // be the result of simple Cartesian interpolation.
        assertEquals(createLineString(new Coordinate(5.0, 53.0), new Coordinate(5.0, 53.00500000211479)),
                subLineString);
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
    void getCoordinateAndBearing_ok() {
        LineString originalLineString = createLineString(FROM, TO);
        CoordinateAndBearing coordinateAndBearing = fractionAndDistanceCalculator.getCoordinateAndBearing(
                originalLineString, 0.5);

        assertThat(coordinateAndBearing.bearing()).isCloseTo(149, Offset.offset(0.5));
    }

    private LineString createLineString(Coordinate... coordinates) {
        return new GeometryFactoryWgs84().createLineString(coordinates);
    }
}
